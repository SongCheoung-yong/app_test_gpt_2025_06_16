package kr.ac.uc.test_2025_05_19_k.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.ac.uc.test_2025_05_19_k.model.request.GroupCreateRequest
import kr.ac.uc.test_2025_05_19_k.repository.GroupRepository
import kr.ac.uc.test_2025_05_19_k.data.local.UserPreference
import kr.ac.uc.test_2025_05_19_k.model.Interest // Interest 모델 import
import kr.ac.uc.test_2025_05_19_k.repository.InterestRepository // InterestRepository import
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    application: Application,
    private val groupRepository: GroupRepository,
    private val interestRepository: InterestRepository // InterestRepository 주입
) : AndroidViewModel(application) {

    private val userPreference = UserPreference(application)

    private val _interests = MutableStateFlow<List<Interest>>(emptyList())
    val interests: StateFlow<List<Interest>> = _interests // 관심사 목록 StateFlow 추가

    init {
        fetchInterests() // ViewModel 초기화 시 관심사 목록 불러오기
    }

    private fun fetchInterests() {
        viewModelScope.launch {
            try {
                val allInterests = interestRepository.getAllInterests()
                _interests.value = allInterests
                Log.d("GroupCreateViewModel", "관심사 목록 불러오기 성공: ${allInterests.size}개")
            } catch (e: Exception) {
                Log.e("GroupCreateViewModel", "관심사 목록 불러오기 실패: ${e.message}")
                _interests.value = emptyList()
            }
        }
    }

    fun createGroup(
        title: String,
        description: String,
        requirements: String,
        category: String, // category는 interest.name이 될 것입니다.
        maxMembers: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val location = userPreference.getLocation()

        val request = GroupCreateRequest(
            title = title,
            description = description,
            requirements = requirements,
            interest = category, // interest로 매핑
            maxMembers = maxMembers,
            locationName = location
        )

        viewModelScope.launch {
            try {
                groupRepository.createGroup(request)
                onSuccess()
            } catch (e: Exception) {
                Log.e("GroupCreate", "생성 실패: ${e.message}")
                onError(e.message ?: "알 수 없는 오류")
            }
        }
    }
}