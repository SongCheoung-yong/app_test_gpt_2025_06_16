package kr.ac.uc.test_2025_05_19_k.model


data class PageGroupNoticeDto(
    val content: List<GroupNoticeDto>,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int,
    val number: Int, // 현재 페이지 번호 (0부터 시작)
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
    val numberOfElements: Int, // content 리스트의 실제 요소 개수
    val pageable: PageableObject? = null, // API 응답에 따라 추가
    val sort: SortObject? = null // API 응답에 따라 추가
)