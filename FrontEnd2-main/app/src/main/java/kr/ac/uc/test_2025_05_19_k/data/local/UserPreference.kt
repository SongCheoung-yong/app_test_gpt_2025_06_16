// app/src/main/java/kr/ac/uc/test_2025_05_19_k/data/local/UserPreference.kt
package kr.ac.uc.test_2025_05_19_k.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreference @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val USER_PREFERENCES_NAME = "user_prefs"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_LOCATION = "location_name" // 지역 정보 저장 키
        // ✅ 추가: 최근 검색어 저장 키
        private const val KEY_RECENT_SEARCHES = "recent_searches"
        private const val MAX_RECENT_SEARCHES = 5 // 저장할 최근 검색어 최대 개수
    }

    /**
     * 사용자의 온보딩 완료 상태를 저장합니다.
     * @param completed 온보딩 완료 여부 (true: 완료, false: 미완료)
     */
    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit {
            putBoolean(KEY_ONBOARDING_COMPLETED, completed)
        }
    }

    /**
     * 사용자의 온보딩 완료 상태를 조회합니다.
     * @return 온보딩 완료 여부. 저장된 값이 없으면 false를 반환합니다.
     */
    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }


    fun saveLocation(location: String) {
        prefs.edit().putString(KEY_LOCATION, location).apply()
    }

    fun getLocation(): String {
        return prefs.getString(KEY_LOCATION, "") ?: ""
    }

    fun clearOnboardingAndLocation() {
        prefs.edit {
            remove(KEY_ONBOARDING_COMPLETED)
            remove(KEY_LOCATION)
        }
    }

    // ✅ 추가: 최근 검색어 저장
    fun addRecentSearch(query: String) {
        val currentSearches = getRecentSearches().toMutableSet()
        currentSearches.remove(query) // 중복 방지를 위해 기존 검색어 삭제
        currentSearches.add(query)

        // 최대 개수를 초과하면 오래된 검색어부터 삭제
        val sortedSearches = currentSearches.toList().sortedDescending().take(MAX_RECENT_SEARCHES).toSet()

        prefs.edit {
            putStringSet(KEY_RECENT_SEARCHES, sortedSearches)
        }
    }

    // ✅ 추가: 최근 검색어 조회
    fun getRecentSearches(): List<String> {
        return prefs.getStringSet(KEY_RECENT_SEARCHES, emptySet())?.toList() ?: emptyList()
    }

    // ✅ 추가: 특정 최근 검색어 삭제
    fun removeRecentSearch(query: String) {
        val currentSearches = getRecentSearches().toMutableSet()
        currentSearches.remove(query)
        prefs.edit {
            putStringSet(KEY_RECENT_SEARCHES, currentSearches)
        }
    }

    // ✅ 추가: 모든 최근 검색어 삭제
    fun clearRecentSearches() {
        prefs.edit {
            remove(KEY_RECENT_SEARCHES)
        }
    }

    // 지역(region) 값 저장 함수
    fun saveRegion(region: String) {
        prefs.edit().putString(KEY_LOCATION, region).apply() // KEY_LOCATION = "location_name"
    }

    // 지역(region) 값 조회 함수
    fun getRegion(): String {
        return prefs.getString(KEY_LOCATION, "") ?: ""
    }

}