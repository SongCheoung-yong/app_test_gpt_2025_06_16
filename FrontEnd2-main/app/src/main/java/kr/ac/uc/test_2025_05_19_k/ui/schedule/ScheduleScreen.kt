// app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/schedule/ScheduleScreen.kt
package kr.ac.uc.test_2025_05_19_k.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(navController: NavController) {
    // 실제 스케줄 화면 구현 필요 (desgin.pdf page 12 참고)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("스케쥴 화면", style = MaterialTheme.typography.headlineMedium)
        Text("달력 및 일정 알림이 표시될 예정입니다.", style = MaterialTheme.typography.bodyLarge)
    }
}