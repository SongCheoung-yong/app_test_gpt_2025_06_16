// app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/group/GroupEditScreen.kt
package kr.ac.uc.test_2025_05_19_k.ui.group

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kr.ac.uc.test_2025_05_19_k.viewmodel.GroupEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupEditScreen(
    navController: NavController,
    groupId: Long, // AppNavGraph로부터 전달받음
    viewModel: GroupEditViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // ViewModel의 상태 관찰
    val groupDetail by viewModel.groupDetail.collectAsState()
    val interests by viewModel.interests.collectAsState() // 전체 관심사 목록
    val isLoading by viewModel.isLoading.collectAsState() // 그룹 정보 로딩 상태
    val isUpdating by viewModel.isUpdating.collectAsState() // 그룹 정보 업데이트 중 상태
    val errorMessage by viewModel.errorMessage.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()

    // UI 입력 필드 상태 (ViewModel의 상태를 사용)
    val title = viewModel.title
    val description = viewModel.description
    val requirements = viewModel.requirements
    val selectedInterestName = viewModel.selectedInterestName
    val maxMembers = viewModel.maxMembers

    var categoryExpanded by remember { mutableStateOf(false) }

    // 그룹 정보 수정 성공 시 이전 화면으로 복귀
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            Toast.makeText(context, "그룹 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    // 오류 메시지 표시
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("그룹 정보 수정") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (groupDetail == null && !isLoading) { // 로딩이 끝났는데 groupDetail이 null이면 오류 또는 정보 없음
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), contentAlignment = Alignment.Center) {
                Text(errorMessage ?: "그룹 정보를 불러올 수 없습니다.", color = MaterialTheme.colorScheme.error)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()) // 내용이 길어질 경우 스크롤 가능하도록
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { viewModel.onTitleChange(it.take(20)) }, // 글자 수 제한은 ViewModel에서 처리하거나 UI에서 take 사용
                    label = { Text("그룹명 (최대 20자)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 관심사(카테고리) 선택 Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedInterestName ?: "선택해주세요", // ViewModel의 selectedInterestName 사용
                        onValueChange = {}, // 직접 수정 불가
                        readOnly = true,
                        label = { Text("카테고리") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor() // 메뉴를 TextField에 연결
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        interests.forEach { interest ->
                            DropdownMenuItem(
                                text = { Text(interest.interestId.toString()) },
                                onClick = {
                                    viewModel.onInterestChange(interest.interestName)
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { viewModel.onDescriptionChange(it.take(500)) },
                    label = { Text("소개 문구 (최대 500자)") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = requirements,
                    onValueChange = { viewModel.onRequirementsChange(it.take(500)) },
                    label = { Text("가입 요구사항 (최대 500자)") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = maxMembers,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) { // 숫자만 입력 가능하도록
                            viewModel.onMaxMembersChange(it.take(2)) // 최대 2자리
                        }
                    },
                    label = { Text("최대 인원 (숫자, 현재 인원 이상)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.updateGroupInfo(
                            onSuccess = {
                                // 성공 시 LaunchedEffect에서 popBackStack 처리
                            },
                            onError = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isUpdating // 업데이트 중이 아닐 때만 활성화
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("수정 완료")
                    }
                }
            }
        }
    }
}