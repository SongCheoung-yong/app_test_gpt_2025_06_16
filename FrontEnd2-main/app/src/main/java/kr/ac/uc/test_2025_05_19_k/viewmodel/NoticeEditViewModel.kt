// app/src/main/java/kr/ac/uc/test_2025_05_19_k/viewmodel/NoticeEditViewModel.kt
package kr.ac.uc.test_2025_05_19_k.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.uc.test_2025_05_19_k.model.request.GroupNoticeCreateRequest
import kr.ac.uc.test_2025_05_19_k.repository.GroupRepository
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class NoticeEditViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val groupId: Long = savedStateHandle.get<Long>("groupId") ?: -1L
    val noticeId: Long = savedStateHandle.get<Long>("noticeId") ?: -1L

    var title by mutableStateOf("")
    var content by mutableStateOf("")

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()

    init {
        // TODO: 수정할 공지사항의 기존 데이터를 불러오는 로직 필요.
        // 현재 API에는 '특정 공지사항 상세 조회'가 없으므로,
        // 임시로 이전 화면에서 데이터를 받아오거나, 제목/내용을 비워둔 채 시작합니다.
        // 또는, 공지사항 목록 조회 API 결과에서 해당 noticeId의 데이터를 찾아 사용할 수 있습니다.
        // 여기서는 간단하게 UI에서 직접 데이터를 전달받는다고 가정하거나, 빈 값으로 시작합니다.
        Log.d("NoticeEditVM", "Init with groupId: $groupId, noticeId: $noticeId")
    }

    // UI에서 기존 데이터를 설정하기 위한 함수
    fun setInitialData(initialTitle: String, initialContent: String) {
        title = initialTitle
        content = initialContent
    }

    fun updateNotice(onError: (String) -> Unit) {
        if (title.isBlank() || content.isBlank()) {
            onError("제목과 내용을 모두 입력해주세요.")
            return
        }
        if (groupId == -1L || noticeId == -1L) {
            onError("잘못된 정보로 수정할 수 없습니다.")
            return
        }

        viewModelScope.launch {
            _isUpdating.value = true
            _updateSuccess.value = false
            try {
                val request = GroupNoticeCreateRequest(title, content)
                val updatedNotice = groupRepository.updateNotice(groupId, noticeId, request)
                Log.d("NoticeEditVM", "공지사항 수정 성공: $updatedNotice")
                _updateSuccess.value = true
            } catch (e: Exception) {
                Log.e("NoticeEditVM", "공지사항 수정 실패: ${e.message}", e)
                onError("공지사항 수정에 실패했습니다: ${e.message}")
            } finally {
                _isUpdating.value = false
            }
        }
    }
}