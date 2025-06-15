package kr.ac.uc.test_2025_05_19_k.ui.gps

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavBackStackEntry
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import androidx.compose.ui.tooling.preview.Preview



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SignInGPSSettingScreen(
    backStackEntry: NavBackStackEntry,
    onBack: () -> Unit = {},
    onLocationGranted: (List<Long>) -> Unit = {}
) {
    // NavBackStackEntry에서 interestIds 파라미터 추출
    val interestIdsParam = backStackEntry.arguments?.getString("interestIds") ?: ""
    val interestIds: List<Long> = interestIdsParam
        .split(",")
        .mapNotNull { it.toLongOrNull() }
        .filter { it > 0 }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // 상단 뒤로가기
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        // 중앙 안내 + "동의" 버튼
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "이제 위치 정보를 확인할게요!",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "위치 정보 확인 동의를 해주세요!",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { locationPermissionState.launchPermissionRequest() }
            ) {
                Text("동의")
            }

            when {
                locationPermissionState.status.isGranted -> {
                    // 권한 허용 즉시 관심사 ID 리스트 콜백
                    SideEffect { onLocationGranted(interestIds) }
                    Text("위치 권한이 허용되었습니다.", color = Color.Green)
                }
                locationPermissionState.status.shouldShowRationale -> {
                    Text("위치 권한이 필요합니다.", color = Color.Red)
                }
                !locationPermissionState.status.isGranted -> {
                    Text("위치 권한이 아직 허용되지 않았습니다.", color = Color.Gray)
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
fun PreviewSignInGPSSettingScreen() {
    // 프리뷰에서는 backStackEntry 없이 간단한 화면 확인만 지원
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text("GPS 권한 설정 프리뷰 화면")
    }
}

