package kr.ac.uc.test_2025_05_19_k.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.ac.uc.test_2025_05_19_k.model.Interest
import kr.ac.uc.test_2025_05_19_k.model.InterestDto
import kr.ac.uc.test_2025_05_19_k.model.ProfileUpdateRequest
import kr.ac.uc.test_2025_05_19_k.network.ApiService
import kr.ac.uc.test_2025_05_19_k.repository.ProfileCacheManager
import kr.ac.uc.test_2025_05_19_k.repository.CachedProfile

@HiltViewModel
class ProfileInputViewModel @Inject constructor(
    private val cacheManager: ProfileCacheManager,
    private val api: ApiService // ApiService를 직접 주입받음
) : ViewModel() {

    // UI 상태 변수
    var interests by mutableStateOf<List<Interest>>(emptyList())
    var interestLoading by mutableStateOf(false)
    var interestError by mutableStateOf<String?>(null)
    var selectedInterestIds by mutableStateOf<List<Long>>(emptyList())
    var name by mutableStateOf("")
    var gender by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var birthYear by mutableStateOf("")
    var locationName by mutableStateOf("")

    // 로딩/에러 상태
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)


    init {
        // 캐시 복원
        cacheManager.loadProfile()?.let {
            name = it.name
            gender = it.gender
            phoneNumber = it.phone
            birthYear = it.birth
            locationName = it.location
        }
        selectedInterestIds = cacheManager.loadInterests()
    }

    /** 관심사 목록 불러오기 - 서버에서 가져옴 */
    fun loadInterests() {
        viewModelScope.launch {
            try {
                interestLoading = true
                interestError = null
                // ApiService의 getAllInterests 사용
                val dtos = api.getAllInterests() // List<InterestDto>
                interests = dtos.map { Interest(it.interestId, it.interestName) }
            } catch (e: Exception) {
                interestError = e.localizedMessage ?: "관심사 불러오기 실패"
            } finally {
                interestLoading = false
            }
        }
    }

    /** 관심사 선택/해제 */
    fun toggleInterest(id: Long) {
        selectedInterestIds = if (selectedInterestIds.contains(id)) {
            selectedInterestIds - id
        } else {
            if (selectedInterestIds.size < 2) selectedInterestIds + id else selectedInterestIds
        }
        cacheManager.saveInterests(selectedInterestIds)
    }

    // 입력값 setter (변경 시 캐시에도 저장)
    fun updateName(newName: String) { name = newName; saveProfileToCache() }
    fun updateGender(newGender: String) { gender = newGender; saveProfileToCache() }
    fun updatePhoneNumber(newNum: String) { phoneNumber = newNum; saveProfileToCache() }
    fun updateBirthYear(newBirth: String) { birthYear = newBirth; saveProfileToCache() }
    fun updateLocation(newLoc: String) { locationName = newLoc; saveProfileToCache() }

    private fun saveProfileToCache() {
        cacheManager.saveProfile(
            CachedProfile(
                name = name,
                gender = gender,
                phone = phoneNumber,
                birth = birthYear,
                location = locationName
            )
        )
    }

    fun submitProfile(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("RegionSettingScreen", "submitProfile start, repository=$api")
        isLoading = true
        errorMessage = null

        // 유효성 체크 (필수)
        val birthYearInt = when {
            birthYear.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> birthYear.substring(0, 4).toIntOrNull()
            else -> birthYear.toIntOrNull()
        }
        if (birthYearInt == null) {
            onError("생년월일(출생연도)을 올바르게 입력하세요.")
            isLoading = false
            return
        }
        if (name.isBlank()) {
            onError("이름을 입력하세요.")
            isLoading = false
            return
        }
        if (gender.isNullOrBlank()) {
            onError("성별을 선택하세요.")
            isLoading = false
            return
        }
        if (phoneNumber.isBlank()) {
            onError("전화번호를 입력하세요.")
            isLoading = false
            return
        }
        if (locationName.isBlank()) {
            onError("위치를 선택하세요.")
            isLoading = false
            return
        }
        if (selectedInterestIds.isEmpty()) {
            onError("관심사를 1개 이상 선택하세요.")
            isLoading = false
            return
        }

        // DTO로 변환
        val profileRequest = ProfileUpdateRequest(
            name = name,
            gender = gender!!,
            phoneNumber = phoneNumber,
            birthYear = birthYearInt,
            locationName = locationName,
            interestIds = selectedInterestIds
        )
        Log.d("ProfileRequest", profileRequest.toString())

        Log.d("RegionSettingScreen", "submitProfile 진입!!") // 여기도 추가

        viewModelScope.launch {
            Log.d("RegionSettingScreen", "코루틴 진입!!")
            try {
                Log.d("RegionSettingScreen", "서버 호출 직전: $profileRequest")
                val result = api.updateProfile(profileRequest)
                Log.d("RegionSettingScreen", "서버 호출 완료: $result")
                if (result.isSuccessful) {
                    Log.d("RegionSettingScreen", "프로필 업데이트 성공!")
                    onSuccess()
                } else {
                    Log.e("RegionSettingScreen", "서버 오류: ${result.code()} / ${result.message()}")
                    onError("서버 오류: ${result.code()} / ${result.message()}")
                }
            } catch (e: Exception) {
                Log.e("RegionSettingScreen", "네트워크 예외 발생: ${e.localizedMessage}")
                onError("네트워크 오류: ${e.localizedMessage ?: "알 수 없는 오류"}")
            } finally {
                isLoading = false
                Log.d("RegionSettingScreen", "submitProfile finally!")
            }
        }
    }





}
