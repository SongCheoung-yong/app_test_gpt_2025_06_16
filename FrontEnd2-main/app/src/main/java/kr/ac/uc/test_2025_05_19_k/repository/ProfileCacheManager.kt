package kr.ac.uc.test_2025_05_19_k.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileCacheManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("profile_cache", Context.MODE_PRIVATE)

    fun saveProfile(profile: CachedProfile) {
        prefs.edit()
            .putString("name", profile.name)
            .putString("gender", profile.gender)
            .putString("phone", profile.phone)
            .putString("birth", profile.birth)
            .putString("location", profile.location)
            .apply()
    }
    fun loadProfile(): CachedProfile? {
        val name = prefs.getString("name", "") ?: ""
        val gender = prefs.getString("gender", "") ?: ""
        val phone = prefs.getString("phone", "") ?: ""
        val birth = prefs.getString("birth", "") ?: ""
        val location = prefs.getString("location", "") ?: ""
        return if (name.isNotBlank() || gender.isNotBlank() || phone.isNotBlank() || birth.isNotBlank() || location.isNotBlank()) {
            CachedProfile(name, gender, phone, birth, location)
        } else null
    }
    fun saveInterests(interests: List<Long>) {
        prefs.edit()
            .putStringSet("interest_ids", interests.map { it.toString() }.toSet())
            .apply()
    }
    fun loadInterests(): List<Long> {
        return prefs.getStringSet("interest_ids", emptySet())?.mapNotNull { it.toLongOrNull() } ?: emptyList()
    }
    fun clear() { prefs.edit().clear().apply() }
}

// 데이터 클래스도 여기 같이 둡니다
data class CachedProfile(
    val name: String,
    val gender: String,
    val phone: String,
    val birth: String,
    val location: String
)
