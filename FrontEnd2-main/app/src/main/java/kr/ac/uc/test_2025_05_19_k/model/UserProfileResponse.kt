// app/src/main/java/kr/ac/uc/test_2025_05_19_k/model/UserProfileResponse.kt
package kr.ac.uc.test_2025_05_19_k.model

/**
 * 사용자 프로필 조회 응답 모델
 * 서버 응답 필드에 맞게 수정/확장 필요
 */
data class UserProfileResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val gender: String,
    val phoneNumber: String,
    val birthYear: Int,
    val profileImage: String?,
    val interests: List<Interest>, // 서버 interest DTO와 맞추세요!
    val groupParticipationCount: Int,
    val attendanceRate: Int,
    val totalMeetings: Int,
    val statsLastUpdated: String,
    val isOwnProfile: Boolean
)

