// app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/group/GroupManagementScreen.kt
package kr.ac.uc.test_2025_05_19_k.ui.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row // Row 임포트
import androidx.compose.foundation.layout.Spacer // Spacer 임포트
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size // size 임포트
import androidx.compose.foundation.layout.width // width 임포트
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons // Icons 임포트
import androidx.compose.material.icons.filled.Edit // Edit 아이콘 임포트
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // IconButton 임포트
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kr.ac.uc.test_2025_05_19_k.model.StudyGroup
import kr.ac.uc.test_2025_05_19_k.ui.common.GroupCard
import kr.ac.uc.test_2025_05_19_k.viewmodel.GroupManagementViewModel
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color

private val tabs = listOf("참여한 그룹", "만든 그룹")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupManagementScreen(
    navController: NavController,
    viewModel: GroupManagementViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> viewModel.fetchMyJoinedGroups()
            1 -> viewModel.fetchMyOwnedGroups()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title, style = MaterialTheme.typography.titleSmall) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        when (selectedTabIndex) {
            0 -> { // 참여한 그룹 탭
                val joinedGroups by viewModel.joinedGroups.collectAsState()
                val isLoading by viewModel.isLoadingJoined.collectAsState()
                val errorMessage by viewModel.errorMessage.collectAsState()
                GroupListContent(
                    groups = joinedGroups,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    navController = navController,
                    emptyListMessage = "참여한 스터디 그룹이 없습니다.",
                    onGroupClick = { groupId ->
                        // 참여한 그룹은 기존 상세 화면으로 이동
                        navController.navigate("group_detail/$groupId")
                    }
                )
            }
            1 -> { // 만든 그룹 탭
                CreatedGroupsTabContent(navController = navController, viewModel = viewModel)
            }
        }
    }
}


@Composable
fun CreatedGroupsTabContent(
    navController: NavController,
    viewModel: GroupManagementViewModel
) {
    val ownedGroups by viewModel.ownedGroups.collectAsState()
    val isLoading by viewModel.isLoadingOwned.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Box를 사용하여 리스트 위에 로딩, 에러, 빈 상태 메시지를 띄우는 구조로 변경
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 스터디 그룹 목록을 표시하는 LazyColumn은 항상 Composition에 포함
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp), // contentPadding으로 변경
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = ownedGroups, key = { group -> group.groupId }) { group ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        GroupCard(group = group) {
                            navController.navigate("group_admin_detail/${group.groupId}")
                        }
                    }
                    IconButton(onClick = {
                        navController.navigate("group_edit/${group.groupId}")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "그룹 정보 수정"
                        )
                    }
                }
            }
        }

        // 상태에 따라 로딩, 에러, 빈 목록 메시지를 LazyColumn 위에 표시
        if (isLoading) {
            CircularProgressIndicator()
        } else errorMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// GroupListContent 함수는 이전과 동일 (참여한 그룹 탭에서 사용)
@Composable
fun GroupListContent(
    groups: List<StudyGroup>,
    isLoading: Boolean,
    errorMessage: String?,
    navController: NavController,
    emptyListMessage: String,
    onGroupClick: (Long) -> Unit
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        }
    } else if (groups.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(text = emptyListMessage, style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = groups, key = { group -> group.groupId }) { group ->
                GroupCard(group = group) {
                    onGroupClick(group.groupId)
                }
            }
        }
    }
}