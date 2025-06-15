package kr.ac.uc.test_2025_05_19_k.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import kr.ac.uc.test_2025_05_19_k.model.Interest
import kr.ac.uc.test_2025_05_19_k.viewmodel.ProfileInputViewModel

@Composable
fun InterestSelectScreenHost(
    navController: NavController,
    viewModel: ProfileInputViewModel = hiltViewModel<ProfileInputViewModel>()
) {
    val interests = viewModel.interests
    val interestLoading = viewModel.interestLoading
    val interestError = viewModel.interestError
    val selectedInterestIds = viewModel.selectedInterestIds
    val userName = viewModel.name // 필요시 이름 전달

    // 관심사 목록 불러오기 (최초 1회)
    LaunchedEffect(Unit) { viewModel.loadInterests() }

    InterestSelectScreen(
        interests = interests,
        selectedIds = selectedInterestIds,
        userName = userName,
        navController = navController,
        onToggle = { viewModel.toggleInterest(it) },
        onNext = {
            // 관심사 선택 완료 후 다음 단계로 이동
            val idsParam = selectedInterestIds.joinToString(",")
            navController.navigate("gps_setting?interestIds=$idsParam")
        },
        isLoading = interestLoading,
        errorMsg = interestError
    )
}

@Composable
fun InterestSelectScreen(
    interests: List<Interest>,
    selectedIds: List<Long>,
    userName: String,
    navController: NavController,
    onToggle: (Long) -> Unit,
    onNext: () -> Unit,
    isLoading: Boolean = false,
    errorMsg: String? = null
) {
    var localErrorMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        // 상단 바
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Button(
                onClick = {
                    if (selectedIds.isEmpty()) {
                        localErrorMsg = "관심사를 1개 이상 선택해 주세요."
                    } else {
                        localErrorMsg = null
                        onNext()
                    }
                },
                enabled = selectedIds.isNotEmpty() && !isLoading,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedIds.isNotEmpty()) Color(0xFF14C7E5) else Color.LightGray
                ),
                modifier = Modifier.height(44.dp)
            ) {
                Text("다음", color = Color.White, fontSize = 17.sp)
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
        Text("반갑습니다 ${userName}님!", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.fillMaxWidth(), color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text("이제부터 ${userName}님의 관심사를 설정할게요!", fontSize = 14.sp, modifier = Modifier.fillMaxWidth(), color = Color.Black)
        Spacer(modifier = Modifier.height(28.dp))

        if (isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (!errorMsg.isNullOrBlank()) {
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        } else {
            InterestCardGrid(
                interests = interests,
                selectedIds = selectedIds,
                onToggle = onToggle
            )
        }

        localErrorMsg?.let {
            Spacer(modifier = Modifier.height(10.dp))
            Text(it, color = Color.Red, fontSize = 13.sp)
        }
    }
}

@Composable
fun InterestCardGrid(
    interests: List<Interest>,
    selectedIds: List<Long>,
    onToggle: (Long) -> Unit
) {
    val rows = interests.chunked(2)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { interest ->
                    InterestCard(
                        interest = interest,
                        selected = selectedIds.contains(interest.interestId),
                        onClick = { onToggle(interest.interestId) }
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.width(140.dp))
                }
            }
        }
    }
}

@Composable
fun InterestCard(
    interest: Interest,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 92.dp)
            .background(
                color = if (selected) Color(0xFF14C7E5) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Color(0xFF14C7E5) else Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = interest.interestName,
            color = if (selected) Color.White else Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}
