package kr.ac.uc.test_2025_05_19_k.model

data class AuthUserProfile(
    val userId: Long,
    val email: String,
    val profileImage: String,
    val oauthId: String,
    val provider: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String
)