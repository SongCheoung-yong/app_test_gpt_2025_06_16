// app/src/main/java/kr/ac/uc/test_2025_05_19_k/viewmodel/NoticeCreateViewModel.kt
package kr.ac.uc.test_2025_05_19_k.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
class NoticeCreateViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    var title by mutableStateOf("")
    var content by mutableStateOf("")

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    private val _createSuccess = MutableStateFlow(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess.asStateFlow()

    fun createNotice(groupId: Long, onError: (String) -> Unit) {
        if (title.isBlank() || content.isBlank()) {
            onError("제목과 내용을 모두 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _isCreating.value = true
            _createSuccess.value = false
            try {
                val request = GroupNoticeCreateRequest(title, content)
                val createdNotice = groupRepository.createNotice(groupId, request)
                Log.d("NoticeCreateVM", "공지사항 생성 성공: $createdNotice")
                _createSuccess.value = true
            } catch (e: Exception) {
                Log.e("NoticeCreateVM", "공지사항 생성 실패: ${e.message}", e)
                onError("공지사항 생성에 실패했습니다: ${e.message}")
            } finally {
                _isCreating.value = false
            }
        }
    }
}