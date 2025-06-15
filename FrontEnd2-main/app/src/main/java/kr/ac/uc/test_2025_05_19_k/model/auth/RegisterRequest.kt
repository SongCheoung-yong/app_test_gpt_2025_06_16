package kr.ac.uc.test_2025_05_19_k.model.auth

data class RegisterRequest(
    val email: String,
    val password: String,
    val nickname: String
)
