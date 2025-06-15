// mo-gag-gong/frontend/frontend-dev-hj/app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/search/SearchResultScreen.kt
package kr.ac.uc.test_2025_05_19_k.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // 이 import가 정확한지 확인해주세요.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kr.ac.uc.test_2025_05_19_k.ui.common.GroupCard
import kr.ac.uc.test_2025_05_19_k.viewmodel.HomeViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import android.util.Log
import kr.ac.uc.test_2025_05_19_k.model.StudyGroup // StudyGroup 모델 임포트 확인

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    navController: NavController,
    searchQuery: String,
    viewModel: HomeViewModel = hiltViewModel(),
    onGroupClick: (Long) -> Unit
) {
    val groupList by viewModel.groupList.collectAsState()
    val isLoading by viewModel.isLoadingInitial.collectAsState()

    LaunchedEffect(searchQuery) {
        Log.d("SearchResultScreen", "검색어: $searchQuery. 그룹 목록 불러오기 시작.")
        viewModel.fetchSearchResults(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("검색 결과", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("group_create") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "그룹 생성")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "\"${searchQuery}\" 검색 결과",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (groupList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("검색 결과가 없습니다.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // items(items = groupList, key = { group -> group.groupId }) // 이전 방식
                    // 아래와 같이 groupList를 첫 번째 인자로 직접 전달합니다.
                    items(
                        items = groupList, // items 파라미터 이름을 명시하는 것이 혼동을 줄 수 있다면, 아래처럼 직접 전달합니다.
                        // items(groupList, // 이렇게만 사용해도 됩니다.
                        key = { group -> group.groupId }
                    ) { group ->
                        GroupCard(group = group) {
                            onGroupClick(group.groupId)
                        }
                    }
                }
            }
        }
    }
}