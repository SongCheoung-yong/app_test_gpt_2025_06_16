package kr.ac.uc.test_2025_05_19_k.model.auth

data class TokenResponse( // 또는 AuthResponse로 클래스명 변경 고려
    val accessToken: String,
    val refreshToken: String,
    val userId: Long, // 추가
)