package kr.ac.uc.test_2025_05_19_k.ui.group.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.ac.uc.test_2025_05_19_k.model.GroupMemberDto
import kr.ac.uc.test_2025_05_19_k.model.GroupNoticeDto
import kr.ac.uc.test_2025_05_19_k.ui.group.GroupGoalListScreen
import kr.ac.uc.test_2025_05_19_k.viewmodel.JoinedGroupDetailViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material3.LinearProgressIndicator
import kr.ac.uc.test_2025_05_19_k.model.GroupGoalDto
import kr.ac.uc.test_2025_05_19_k.ui.group.ChatTabScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun JoinedGroupDetailScreen(
    navController: NavController,
    viewModel: JoinedGroupDetailViewModel = hiltViewModel()
) {
    val groupTitle by viewModel.groupTitle.collectAsState()
    val notices by viewModel.notices.collectAsState()
    val members by viewModel.members.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    val tabs = listOf("공지사항", "멤버", "그룹 목표", "채팅", "모임")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.leaveGroupEvent.collectLatest {
            navController.navigateUp()
        }
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            when (pagerState.currentPage) {
                0 -> viewModel.loadNotices()
                1 -> viewModel.loadMembers()
                2 -> viewModel.loadGoals()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(groupTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    FilterChip(
                        selected = (pagerState.currentPage == index),
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        label = { Text(title) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Black,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = null
                    )
                }
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> NoticesTabContent(notices = notices)
                    1 -> MembersTabContent(
                        members = members,
                        currentUserId = currentUserId,
                        onLeaveClick = { viewModel.leaveGroup() },
                        onMemberClick = { memberId ->
                            navController.navigate("group_member_detail/${viewModel.groupId}/${memberId}/ACTIVE")
                        }
                    )
                    2 -> GoalsTabContent(
                        navController = navController,
                        goals = goals,
                        groupId = viewModel.groupId
                    )
                    3 -> ChatTabScreen(navController = navController, groupId = viewModel.groupId)
                    4 -> PlaceholderContent(text = "모임 기능은 준비 중입니다.")
                }
            }
        }
    }
}

// 공지사항 탭 컨텐츠... (이전과 동일)
@Composable
fun NoticesTabContent(notices: List<GroupNoticeDto>) {
    if (notices.isEmpty()) {
        PlaceholderContent(text = "등록된 공지사항이 없습니다.")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(notices) { notice ->
                ParticipantNoticeCard(notice = notice)
            }
        }
    }
}

@Composable
fun GoalsTabContent(
    navController: NavController,
    goals: List<GroupGoalDto>,
    groupId: Long
) {
    if (goals.isEmpty()) {
        PlaceholderContent(text = "등록된 그룹 목표가 없습니다.")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(goals) { goal ->
                ParticipantGoalItem(goal = goal) {
                    navController.navigate("group_goal_detail/${groupId}/${goal.goalId}?isAdmin=false")
                }
            }
        }
    }
}

// [신규] 참여자용 그룹 목표 아이템 카드
@Composable
fun ParticipantGoalItem(goal: GroupGoalDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // 카드 클릭 기능
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(goal.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${goal.startDate} ~ ${goal.endDate}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 진행률 표시
            LinearProgressIndicator(
                progress = { goal.completedCount.toFloat() / goal.totalCount.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "달성률: ${goal.completedCount} / ${goal.totalCount}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ParticipantNoticeCard(notice: GroupNoticeDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(notice.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "작성자: ${notice.creatorName}  •  ${notice.createdAt.take(10)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(notice.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


// 멤버 탭 컨텐츠
@Composable
fun MembersTabContent(
    members: List<GroupMemberDto>,
    currentUserId: Long?,
    onLeaveClick: () -> Unit,
    onMemberClick: (Long) -> Unit // [추가] 멤버 ID를 받는 클릭 핸들러
) {
    if (members.isEmpty()) {
        PlaceholderContent(text = "멤버 정보를 불러오는 중입니다...")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(members) { member ->
                MemberItem(
                    member = member,
                    isCurrentUser = member.userId == currentUserId,
                    onLeaveClick = onLeaveClick,
                    onMemberClick = { onMemberClick(member.userId) } // [수정] 클릭 시 멤버 ID 전달
                )
            }
        }
    }
}

// 멤버 아이템
@OptIn(ExperimentalMaterial3Api::class) // [추가] Card의 onClick을 위해 필요
@Composable
fun MemberItem(
    member: GroupMemberDto,
    isCurrentUser: Boolean,
    onLeaveClick: () -> Unit,
    onMemberClick: () -> Unit // [추가] 클릭 핸들러
) {
    // [수정] Card에 onClick 속성 추가
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onMemberClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = member.profileImage ?: "https://via.placeholder.com/150",
                contentDescription = "멤버 프로필 사진",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(member.userName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "가입일: ${member.joinDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (isCurrentUser) {
                // '탈퇴' 버튼은 카드 전체 클릭에 방해되지 않도록 별도 클릭 핸들러를 가짐
                TextButton(onClick = onLeaveClick) {
                    Text("탈퇴", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}


// 임시 컨텐츠 ... (이전과 동일)
@Composable
fun PlaceholderContent(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}