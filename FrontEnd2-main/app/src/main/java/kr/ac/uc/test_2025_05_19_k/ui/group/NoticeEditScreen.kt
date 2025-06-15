// app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/group/NoticeEditScreen.kt
package kr.ac.uc.test_2025_05_19_k.ui.group

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kr.ac.uc.test_2025_05_19_k.viewmodel.NoticeEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeEditScreen(
    navController: NavController,
    groupId: Long,
    noticeId: Long,
    initialTitle: String, // 네비게이션 시 전달받은 기존 제목
    initialContent: String, // 네비게이션 시 전달받은 기존 내용
    viewModel: NoticeEditViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isUpdating by viewModel.isUpdating.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()

    // Composable이 처음 시작될 때 ViewModel에 초기 데이터 설정
    LaunchedEffect(Unit) {
        viewModel.setInitialData(initialTitle, initialContent)
    }

    // 수정 성공 시 이전 화면으로 복귀
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            Toast.makeText(context, "공지사항이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            // popBackStack을 두 번 호출하여 상세화면까지 돌아가도록 할 수 있습니다.
            // 또는 이전 화면에서 결과를 받아 새로고침 하도록 구현해야 합니다.
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("공지사항 수정") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.updateNotice { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = !isUpdating
                    ) {
                        if (isUpdating) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("수정")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = viewModel.content,
                onValueChange = { viewModel.content = it },
                label = { Text("내용") },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
    }
}