
package kr.ac.uc.test_2025_05_19_k.ui.profile

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.withStyle
import kr.ac.uc.test_2025_05_19_k.R
import kr.ac.uc.test_2025_05_19_k.repository.TokenManager

import dagger.hilt.android.EntryPointAccessors
import android.app.Application // Application import 추가
import dagger.hilt.android.qualifiers.ApplicationContext


@Composable
fun SignInScreen(onNavigateNext: () -> Unit = {}) {
    val context = LocalContext.current



    // Hilt를 통해 TokenManager 인스턴스 가져오기
    val tokenManager = remember {
        val app = context.applicationContext as Application
        EntryPointAccessors.fromApplication(app, TokenManagerEntryPoint::class.java).getTokenManager()
    }


    // ① 토큰이 이미 있으면 자동으로 다음 화면 이동
    LaunchedEffect(Unit) {
        if (tokenManager.hasValidToken()) {
            onNavigateNext()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.log),
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Mo-Gag-Gong", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Create an account\n")
                }
                append("Enter your email to sign up for this app")
            },
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        GoogleLoginButton {
            Google_Login(context)

        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            buildAnnotatedString {
                append("By clicking continue, you agree to our")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" Terms of Service and Privacy Policy ")
                }
            },
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GoogleLoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB4B4B4))
    ) {
        Image(
            painter = painterResource(id = R.drawable.log),
            contentDescription = "Google Logo",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Continue with Google", color = Color.Black)
    }
}

// 🌐 Google OAuth 로그인 실행
fun Google_Login(context: Context) {
    val loginUrl =
        "http://springboot-developer-env.eba-mikwqecm.ap-northeast-2.elasticbeanstalk.com/oauth2/authorization/google"

    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()

    customTabsIntent.launchUrl(context, Uri.parse(loginUrl))
}


@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
@dagger.hilt.EntryPoint
interface TokenManagerEntryPoint {
    fun getTokenManager(): TokenManager
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    SignInScreen()
}