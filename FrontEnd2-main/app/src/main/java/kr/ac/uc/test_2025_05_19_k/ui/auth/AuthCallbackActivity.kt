// app/src/main/java/kr/ac/uc/test_2025_05_19_k/ui/auth/AuthCallbackActivity.kt
package kr.ac.uc.test_2025_05_19_k.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.uc.test_2025_05_19_k.MainActivity
import kr.ac.uc.test_2025_05_19_k.repository.TokenManager
import javax.inject.Inject

@AndroidEntryPoint // Hilt 의존성 주입을 위해 필요
class AuthCallbackActivity : AppCompatActivity() {

    @Inject
    lateinit var tokenManager: TokenManager // TokenManager 주입

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent?.data
        if (uri != null && uri.toString().startsWith("com.mogacko://oauth2callback")) {
            val accessToken = uri.getQueryParameter("accessToken")
            val refreshToken = uri.getQueryParameter("refreshToken")
            val userIdString = uri.getQueryParameter("userId")

            if (!accessToken.isNullOrBlank() && !userIdString.isNullOrBlank()) {
                try {
                    val userIdLong = userIdString.toLong() // userId를 Long으로 변환

                    // TokenManager로 토큰/유저ID 저장
                    tokenManager.saveTokens(accessToken, refreshToken ?: "", userIdLong)

                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                    // 로그 출력
                    Log.d("AuthCallback", "Tokens saved via TokenManager.")
                    Log.d("AuthCallback", "accessToken: $accessToken")
                    Log.d("AuthCallback", "refreshToken: $refreshToken")
                    Log.d("AuthCallback", "userId: $userIdLong")

                } catch (e: NumberFormatException) {
                    Log.e("AuthCallback", "Failed to parse userId: $userIdString", e)
                    Toast.makeText(this, "로그인 실패: 사용자 ID 오류", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.w("AuthCallback", "Login failed: accessToken or userId is null or blank.")
                Toast.makeText(this, "로그인 실패: 토큰 또는 사용자 ID 없음", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.w("AuthCallback", "Invalid URI or not an OAuth callback: $uri")
        }

        // 메인 화면(MainActivity)으로 이동
        val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(mainActivityIntent)
        finish()
    }
}
