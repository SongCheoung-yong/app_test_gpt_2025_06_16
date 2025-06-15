package kr.ac.uc.test_2025_05_19_k.model


//추가 생성
data class ProfileUpdateRequest(
    val name: String,
    val gender: String,
    val phoneNumber: String,
    val birthYear: Int,
    val locationName: String,
    val interestIds: List<Long>
)
