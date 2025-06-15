// app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/profile/MyProfileScreen.kt
package kr.ac.uc.test_2025_05_19_k.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MyProfileScreen(navController: NavController) {
    // 실제 내 프로필 화면 구현 필요 (desgin.pdf page 42 참고)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("마이프로필 화면", style = MaterialTheme.typography.headlineMedium)
        Text("사용자 프로필 정보 및 통계가 표시될 예정입니다.", style = MaterialTheme.typography.bodyLarge)
    }
}