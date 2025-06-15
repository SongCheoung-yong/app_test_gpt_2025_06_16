package kr.ac.uc.test_2025_05_19_k.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kr.ac.uc.test_2025_05_19_k.R
import kr.ac.uc.test_2025_05_19_k.viewmodel.ProfileInputViewModel

@Composable
fun SignInProfileSettingScreen(
    navController: NavController,
    viewModel: ProfileInputViewModel = hiltViewModel(),
    onPrev: () -> Unit = {},
    onNext: (name: String, gender: String, phone: String, birth: String) -> Unit = { _, _, _, _ -> }
) {
    // ViewModel의 상태 바로 참조 (자동 캐시 복원값 포함)
    val name = viewModel.name
    val gender = viewModel.gender ?: ""
    val phoneNumber = viewModel.phoneNumber
    val birthYear = viewModel.birthYear

    // 유효성 검사
    val isNameValid = name.isNotBlank()
    val isGenderValid = gender.isNotBlank()
    val phoneDigits = phoneNumber.replace("-", "")
    val isPhoneValid = phoneDigits.matches(Regex("^01[0-9]{8,9}$"))
    val isBirthValid = birthYear.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) &&
            runCatching { java.time.LocalDate.parse(birthYear) }.isSuccess
    val isFormValid = isNameValid && isGenderValid && isPhoneValid && isBirthValid

    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedContainerColor = Color(0xFFF1F1F1),
        unfocusedContainerColor = Color(0xFFF1F1F1)
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 앱바
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrev) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Button(
                onClick = {
                    if (isFormValid) {
                        onNext(name, gender, phoneNumber, birthYear)
                    }
                },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color.Cyan else Color.LightGray
                )
            ) {
                Text("다음", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("모각공에 오신 것을 환영합니다!", fontWeight = FontWeight.Bold)
        Text("이제부터 당신에 대해 알려주세요!", fontSize = 13.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // 캐릭터 이미지 (예시)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.log),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 이름
        InputLabel("이름")
        TextField(
            value = name,
            onValueChange = { viewModel.updateName(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("이름을 입력하세요") },
            colors = textFieldColors,
            singleLine = true
        )
        if (!isNameValid && name.isNotEmpty()) {
            Text("이름을 입력하세요.", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 성별
        InputLabel("성별")
        Row(modifier = Modifier.fillMaxWidth()) {
            GenderButton("남", gender == "남") { viewModel.updateGender("남") }
            Spacer(modifier = Modifier.width(8.dp))
            GenderButton("여", gender == "여") { viewModel.updateGender("여") }
        }
        if (!isGenderValid) {
            Text("성별을 선택하세요.", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 전화번호
        InputLabel("전화번호")
        TextField(
            value = phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("전화번호 입력") },
            colors = textFieldColors,
            singleLine = true
        )
        if (phoneNumber.isNotBlank() && !isPhoneValid) {
            Text("올바른 전화번호 형식(01012345678 또는 010-1234-5678)을 입력하세요.", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 생년월일
        InputLabel("생년월일")
        TextField(
            value = birthYear,
            onValueChange = { viewModel.updateBirthYear(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("예: 2004-05-24") },
            colors = textFieldColors,
            singleLine = true
        )
        if (birthYear.isNotBlank() &&
            (!birthYear.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) || runCatching { java.time.LocalDate.parse(birthYear) }.isFailure)
        ) {
            Text("생년월일을 YYYY-MM-DD 형식으로 입력하세요.", color = Color.Red, fontSize = 12.sp)
        }
    }
}

@Composable
fun InputLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
fun RowScope.GenderButton(label: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color.Cyan else Color.LightGray
        )
    ) {
        Text(label, color = if (selected) Color.White else Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileInputScreen() {
    // 미리보기에서는 NavController가 필요하므로 임시로 생성
    val navController = androidx.navigation.compose.rememberNavController()
    SignInProfileSettingScreen(navController = navController)
}
