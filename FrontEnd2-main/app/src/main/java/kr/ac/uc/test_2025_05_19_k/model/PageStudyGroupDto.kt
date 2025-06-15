// app/src/main/java/kr/ac/uc/test_2025_05_19_k/model/PageStudyGroupDto.kt
package kr.ac.uc.test_2025_05_19_k.model

// StudyGroup은 이미 임포트 되어 있다고 가정합니다.
// import kr.ac.uc.test_2025_05_19_k.model.StudyGroup

/**
 * Spring Pageable 객체에 대한 정보를 담는 DTO
 * API 명세에 따라 필드 정의
 */
data class PageableObject(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: SortObject, // SortObject 참조
    val offset: Long,
    val paged: Boolean,
    val unpaged: Boolean
)

/**
 * Spring Sort 객체에 대한 정보를 담는 DTO
 * API 명세에 따라 필드 정의
 */
data class SortObject(
    val sorted: Boolean,
    val unsorted: Boolean,
    val empty: Boolean
)

data class PageStudyGroupDto(
    val content: List<StudyGroup>,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int, // 현재 페이지의 크기 (요청한 size와 동일)
    val number: Int, // 현재 페이지 번호 (0부터 시작)
    val first: Boolean, // 첫 페이지 여부
    val last: Boolean,  // 마지막 페이지 여부
    val empty: Boolean, // 현재 페이지가 비어있는지 여부
    val numberOfElements: Int, // 현재 페이지의 실제 요소 개수

    // 추가된 필드 (API_Final.md 스키마 참조)
    val pageable: PageableObject,
    val sort: SortObject
)