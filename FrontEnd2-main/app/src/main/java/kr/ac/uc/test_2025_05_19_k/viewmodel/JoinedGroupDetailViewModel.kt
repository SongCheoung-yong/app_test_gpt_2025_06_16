package kr.ac.uc.test_2025_05_19_k.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.uc.test_2025_05_19_k.model.GroupGoalDto
import kr.ac.uc.test_2025_05_19_k.model.GroupMemberDto
import kr.ac.uc.test_2025_05_19_k.model.GroupNoticeDto
import kr.ac.uc.test_2025_05_19_k.repository.GroupRepository
import kr.ac.uc.test_2025_05_19_k.repository.TokenManager
import javax.inject.Inject

@HiltViewModel
class JoinedGroupDetailViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val tokenManager: TokenManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val groupId: Long = checkNotNull(savedStateHandle["groupId"])

    // --- UI 상태 정의 (StateFlow) ---
    private val _groupTitle = MutableStateFlow("스터디 그룹")
    val groupTitle = _groupTitle.asStateFlow()

    private val _notices = MutableStateFlow<List<GroupNoticeDto>>(emptyList())
    val notices = _notices.asStateFlow()

    private val _members = MutableStateFlow<List<GroupMemberDto>>(emptyList())
    val members = _members.asStateFlow()

    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    private val _leaveGroupEvent = MutableSharedFlow<Unit>()
    val leaveGroupEvent = _leaveGroupEvent.asSharedFlow()

    private val _goals = MutableStateFlow<List<GroupGoalDto>>(emptyList())
    val goals = _goals.asStateFlow()

    // --- 초기화 로직 ---
    init {
        loadGroupDetails()
        loadCurrentUserId()
    }

    // --- 데이터 로딩 함수 ---
    fun loadGroupDetails() {
        viewModelScope.launch {
            try {
                val detail = groupRepository.getGroupDetail(groupId)
                _groupTitle.value = detail.title
            } catch (e: Exception) {
                Log.e("JoinedGroupDetailVM", "그룹 상세 정보 로드 실패", e)
            }
        }
    }

    fun loadGoals() {
        viewModelScope.launch {
            try {
                // [수정] Long 타입인 groupId를 String으로 변환하여 전달
                _goals.value = groupRepository.getGroupGoals(groupId.toString()) ?: emptyList()
            } catch (e: Exception) {
                Log.e("JoinedGroupDetailVM", "그룹 목표 로드 실패", e)
                _goals.value = emptyList()
            }
        }
    }

    fun loadNotices() {
        viewModelScope.launch {
            try {
                val response = groupRepository.getGroupNotices(groupId, 0, 20)
                _notices.value = response.content
            } catch (e: Exception) {
                Log.e("JoinedGroupDetailVM", "공지사항 로드 실패", e)
                _notices.value = emptyList()
            }
        }
    }

    fun loadMembers() {
        viewModelScope.launch {
            // [수정] Result 타입은 onSuccess, onFailure로 결과값을 처리해야 합니다.
            groupRepository.getGroupMembers(groupId)
                .onSuccess { memberList ->
                    _members.value = memberList
                }
                .onFailure { e ->
                    Log.e("JoinedGroupDetailVM", "멤버 목록 로드 실패", e)
                    _members.value = emptyList()
                }
        }
    }

    private fun loadCurrentUserId() {
        // [수정] getUserId()는 Flow가 아니므로, firstOrNull() 없이 바로 값을 할당합니다.
        _currentUserId.value = tokenManager.getUserId()
    }

    // --- 사용자 액션 함수 ---
    fun leaveGroup() {
        viewModelScope.launch {
            try {
                groupRepository.leaveGroup(groupId)
                _leaveGroupEvent.emit(Unit)
            } catch (e: Exception) {
                Log.e("JoinedGroupDetailVM", "그룹 탈퇴 실패", e)
            }
        }
    }
}