// app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/group/NoticeCreateScreen.kt
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
import kr.ac.uc.test_2025_05_19_k.viewmodel.NoticeCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeCreateScreen(
    navController: NavController,
    groupId: Long,
    viewModel: NoticeCreateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isCreating by viewModel.isCreating.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()

    // 생성 성공 시 이전 화면으로 복귀
    LaunchedEffect(createSuccess) {
        if (createSuccess) {
            // 이전 화면(GroupAdminDetailScreen)의 공지사항 목록을 새로고침하도록 신호를 보낼 수 있으면 더 좋습니다.
            // 여기서는 간단히 뒤로가기만 구현합니다.
            Toast.makeText(context, "공지사항이 등록되었습니다.", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("공지사항 작성") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.createNotice(groupId) { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = !isCreating
                    ) {
                        if (isCreating) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("완료")
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
                modifier = Modifier.fillMaxWidth().weight(1f) // 남은 공간 모두 차지
            )
        }
    }
}