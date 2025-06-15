// app/src/main/java/kr/ac/uc/test_2025_05_19_k/viewmodel/GroupDetailViewModel.kt
package kr.ac.uc.test_2025_05_19_k.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.ac.uc.test_2025_05_19_k.model.StudyGroupDetail
import kr.ac.uc.test_2025_05_19_k.repository.GroupRepository
// import kr.ac.uc.test_2025_05_19_k.repository.TokenManager // TokenManager 제거
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    // private val tokenManager: TokenManager, // TokenManager 제거
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val groupId: Long = savedStateHandle.get<Long>("groupId") ?: -1L

    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()

    private val _groupDetail = MutableStateFlow<StudyGroupDetail?>(null)
    val groupDetail: StateFlow<StudyGroupDetail?> = _groupDetail.asStateFlow()

    private val _detailErrorMessage = MutableStateFlow<String?>(null)
    val detailErrorMessage: StateFlow<String?> = _detailErrorMessage.asStateFlow()

    private val _applySuccess = MutableStateFlow<Boolean?>(null)
    val applySuccess: StateFlow<Boolean?> = _applySuccess.asStateFlow()

    // 그룹장 여부 판단 로직 제거
    // private val _isCurrentUserCreator = MutableStateFlow(false)
    // val isCurrentUserCreator: StateFlow<Boolean> = _isCurrentUserCreator.asStateFlow()

    init {
        Log.d("GroupDetailVM", "ViewModel init. groupId: $groupId")
        if (groupId != -1L) {
            loadGroupDetail() // 공지사항 로드 로직 제거, 원래대로 loadGroupDetail만 호출
        } else {
            _detailErrorMessage.value = "유효하지 않은 그룹 ID 입니다."
            Log.e("GroupDetailVM", "유효하지 않은 groupId로 ViewModel 초기화됨: $groupId")
        }
    }

    fun loadGroupDetail() {
        viewModelScope.launch {
            _isLoadingDetail.value = true
            _detailErrorMessage.value = null
            Log.d("GroupDetailVM", "loadGroupDetail 호출됨 (ID: $groupId)")
            try {
                val detail = groupRepository.getGroupDetail(groupId)
                _groupDetail.value = detail
                Log.d("GroupDetailVM", "그룹 상세 정보 로드 성공: $detail")

                // 그룹장 여부 판단 로직 제거
                // val currentUserId = tokenManager.getUserId()
                // if (detail != null && currentUserId != null) {
                //     _isCurrentUserCreator.value = (detail.creatorId == currentUserId)
                // }

            } catch (e: Exception) {
                Log.e("GroupDetailVM", "그룹 상세 정보 로드 실패: ${e.message}", e)
                _detailErrorMessage.value = "그룹 상세 정보를 불러오지 못했습니다: ${e.message}"
                _groupDetail.value = null
            } finally {
                _isLoadingDetail.value = false
            }
        }
    }

    fun applyToGroup() {
        if (groupId == -1L || _groupDetail.value == null) return
        viewModelScope.launch {
            try {
                groupRepository.applyToGroup(groupId)
                _applySuccess.value = true
                _groupDetail.update { it?.copy(alreadyApplied = true) }
            } catch (e: Exception) {
                _applySuccess.value = false
            }
        }
    }

    fun resetApplyStatus() {
        _applySuccess.value = null
    }
}