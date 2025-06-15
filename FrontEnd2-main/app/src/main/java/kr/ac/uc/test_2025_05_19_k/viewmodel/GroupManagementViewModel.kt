// app/src/main/java/kr/ac/uc/test_2025_05_19_k/viewmodel/GroupManagementViewModel.kt
package kr.ac.uc.test_2025_05_19_k.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.uc.test_2025_05_19_k.model.StudyGroup
import kr.ac.uc.test_2025_05_19_k.repository.GroupRepository
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class GroupManagementViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _joinedGroups = MutableStateFlow<List<StudyGroup>>(emptyList())
    val joinedGroups: StateFlow<List<StudyGroup>> = _joinedGroups.asStateFlow()

    private val _ownedGroups = MutableStateFlow<List<StudyGroup>>(emptyList())
    val ownedGroups: StateFlow<List<StudyGroup>> = _ownedGroups.asStateFlow()

    private val _isLoadingJoined = MutableStateFlow(false)
    val isLoadingJoined: StateFlow<Boolean> = _isLoadingJoined.asStateFlow()

    private val _isLoadingOwned = MutableStateFlow(false)
    val isLoadingOwned: StateFlow<Boolean> = _isLoadingOwned.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun fetchMyJoinedGroups() {
        viewModelScope.launch {
            _isLoadingJoined.value = true
            _errorMessage.value = null
            try {
                val groups = groupRepository.getMyJoinedGroups() // 실제 API 호출로 변경
                _joinedGroups.value = groups
                Log.d("GroupMgmtViewModel", "참여한 그룹 목록 로드 완료: ${groups.size}개")
            } catch (e: Exception) {
                Log.e("GroupMgmtViewModel", "참여한 그룹 목록 로드 실패: ${e.message}", e)
                _errorMessage.value = "참여한 그룹 목록을 불러오는 중 오류가 발생했습니다."
                _joinedGroups.value = emptyList()
            } finally {
                _isLoadingJoined.value = false
            }
        }
    }

    fun fetchMyOwnedGroups() {
        viewModelScope.launch {
            _isLoadingOwned.value = true
            _errorMessage.value = null
            try {
                val groups = groupRepository.getMyOwnedGroups() // 실제 API 호출로 변경
                _ownedGroups.value = groups
                Log.d("GroupMgmtViewModel", "만든 그룹 목록 로드 완료: ${groups.size}개")
            } catch (e: Exception) {
                Log.e("GroupMgmtViewModel", "만든 그룹 목록 로드 실패: ${e.message}", e)
                _errorMessage.value = "만든 그룹 목록을 불러오는 중 오류가 발생했습니다."
                _ownedGroups.value = emptyList()
            } finally {
                _isLoadingOwned.value = false
            }
        }
    }
}