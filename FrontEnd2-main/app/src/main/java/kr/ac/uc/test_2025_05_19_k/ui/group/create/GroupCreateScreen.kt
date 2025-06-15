package kr.ac.uc.test_2025_05_19_k.ui.group.create

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
// M2 imports - to be replaced or used carefully if needed for specific M2 components not yet migrated
// import androidx.compose.material.* // Comment out or remove if fully migrating to M3 for these components

// M3 imports
import androidx.compose.material3.Button // M3 Button
import androidx.compose.material3.DropdownMenuItem // M3 DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api // If you use experimental M3 APIs
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon // M3 Icon
import androidx.compose.material3.OutlinedTextField // M3 OutlinedTextField
import androidx.compose.material3.Text // M3 Text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kr.ac.uc.test_2025_05_19_k.viewmodel.GroupCreateViewModel
import kr.ac.uc.test_2025_05_19_k.model.Interest

// No need to import menuAnchor separately if using ExposedDropdownMenuDefaults directly
// import androidx.compose.material3.ExposedDropdownMenuDefaults.menuAnchor // This line can be removed

@OptIn(ExperimentalMaterial3Api::class) // Use ExperimentalMaterial3Api for M3
@Composable
fun GroupCreateScreen(
    navController: NavController,
    viewModel: GroupCreateViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("선택") }

    val interests by viewModel.interests.collectAsState()
    var maxMembers by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField( // This should now be androidx.compose.material3.OutlinedTextField
            value = title,
            onValueChange = { if (it.length <= 20) title = it },
            label = { Text("그룹명 (최대 20자)") }, // androidx.compose.material3.Text
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField( // androidx.compose.material3.OutlinedTextField
            value = description,
            onValueChange = { if (it.length <= 500) description = it },
            label = { Text("소개 문구 (최대 500자)") }, // androidx.compose.material3.Text
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField( // androidx.compose.material3.OutlinedTextField
            value = requirements,
            onValueChange = { if (it.length <= 500) requirements = it },
            label = { Text("가입 요구사항 (최대 500자)") }, // androidx.compose.material3.Text
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField( // androidx.compose.material3.OutlinedTextField
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("카테고리") }, // androidx.compose.material3.Text
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) }, // androidx.compose.material3.Icon
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // This should now resolve correctly
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                interests.forEach { interest ->
                    DropdownMenuItem( // androidx.compose.material3.DropdownMenuItem
                        text = { Text(interest.interestName) }, // androidx.compose.material3.Text
                        onClick = {
                            selectedCategory = interest.interestName
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField( // androidx.compose.material3.OutlinedTextField
            value = maxMembers,
            onValueChange = { if (it.all { ch -> ch.isDigit() } && it.length <= 2) maxMembers = it },
            label = { Text("최대 인원 (숫자)") }, // androidx.compose.material3.Text
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button( // androidx.compose.material3.Button
            onClick = {
                val members = maxMembers.toIntOrNull() ?: 0
                if (
                    title.isNotBlank() && selectedCategory != "선택" && members in 2..99
                ) {
                    viewModel.createGroup(
                        title = title,
                        description = description,
                        requirements = requirements,
                        category = selectedCategory,
                        maxMembers = members,
                        onSuccess = { navController.popBackStack() },
                        onError = { error ->
                            Log.e("GroupCreateScreen", "그룹 생성 실패: $error")
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("그룹 생성") // androidx.compose.material3.Text
        }
    }
}