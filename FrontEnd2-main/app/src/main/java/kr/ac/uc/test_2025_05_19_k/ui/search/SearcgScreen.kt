// mo-gag-gong/frontend/frontend-dev-hj/app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/search/SearcgScreen.kt
package kr.ac.uc.test_2025_05_19_k.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kr.ac.uc.test_2025_05_19_k.viewmodel.HomeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    onSearch: (String) -> Unit
) {
    val region by viewModel.region.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    // ✅ 변경: 하드코딩된 최근 검색어 대신 ViewModel의 StateFlow 사용
    val recentSearches by viewModel.recentSearches.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initUser() // 지역명 로드를 위해 초기화
        viewModel.loadRecentSearches() // ✅ 추가: 화면 진입 시 최근 검색어 로드
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // 검색 바
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("${region} 근처에서 검색", color = Color.Gray) },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "검색 아이콘")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            if (searchQuery.isNotBlank()) {
                                viewModel.addRecentSearch(searchQuery) // ✅ 추가: 검색 시 최근 검색어에 추가
                                onSearch(searchQuery)
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "검색 실행")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            viewModel.addRecentSearch(searchQuery) // ✅ 추가: 검색 시 최근 검색어에 추가
                            onSearch(searchQuery)
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 최근 검색어
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "최근 검색어",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { viewModel.clearAllRecentSearches() }) { // ✅ 변경: 모든 최근 검색어 삭제
                    Text("모두 지우기", color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recentSearches.forEach { search ->
                    AssistChip(
                        onClick = {
                            searchQuery = search // 검색 창에 최근 검색어 입력
                            onSearch(search)
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(search)
                                Spacer(Modifier.width(4.dp))
                                IconButton(
                                    onClick = { viewModel.removeRecentSearch(search) }, // ✅ 변경: 개별 삭제
                                    modifier = Modifier.size(16.dp).align(Alignment.CenterVertically) // 아이콘 중앙 정렬
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "삭제")
                                }
                            }
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFE0E0E0),
                            labelColor = Color.Black
                        )
                    )
                }
            }
        }
    }
}