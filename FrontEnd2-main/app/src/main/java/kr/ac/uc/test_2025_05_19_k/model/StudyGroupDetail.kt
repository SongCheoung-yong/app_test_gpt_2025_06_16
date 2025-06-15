package kr.ac.uc.test_2025_05_19_k.model

data class StudyGroupDetail(
    val groupId: Long, // id -> groupId
    val creatorId: Long, // 추가
    val creatorName: String, // 추가
    val title: String, // name -> title
    val interestName: String, // category -> interestName
    val description: String,
    val locationName: String, // 추가
    val startDate: String?, // 추가
    val endDate: String?, // 추가
    val maxMembers: Int,
    val currentMembers: Int, // memberCount -> currentMembers
    val requirements: String, // 추가
    val isActive: Boolean, // 추가
    val createdAt: String, // 추가
    val updatedAt: String, // 추가
    val alreadyApplied: Boolean // 클라이언트 전용 상태 유지 가능
)

