// app/src/main/java/kr/ac/uc/test_2025_05_19_k/model/GroupNoticeDto.kt
package kr.ac.uc.test_2025_05_19_k.model

data class GroupNoticeDto(
    val noticeId: Long,
    val groupId: Long,
    val creatorId: Long,
    val creatorName: String,
    val title: String,
    val content: String,
    val createdAt: String, // "yyyy-MM-dd'T'HH:mm:ss" 형식의 문자열 또는 적절한 날짜 타입
    val updatedAt: String
)