// mo-gag-gong/frontend/frontend-dev-hj/app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/common/GroupCard.kt
package kr.ac.uc.test_2025_05_19_k.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.ac.uc.test_2025_05_19_k.model.StudyGroup

@Composable
fun GroupCard(
    group: StudyGroup,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = group.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = group.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text(
                    text = "#${group.interestName}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 12.dp)
                )

                // 현재 인원과 최대 인원을 함께 표시
                Text(
                    text = "인원 ${group.currentMembers}/${group.maxMembers}명", // 수정된 부분
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}