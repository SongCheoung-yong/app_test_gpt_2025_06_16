// app/src/main/java/kr/ac/uc/test_2025_05_19_k/repository/InterestRepository.kt
package kr.ac.uc.test_2025_05_19_k.repository

import android.util.Log
import kr.ac.uc.test_2025_05_19_k.model.Interest
import kr.ac.uc.test_2025_05_19_k.network.ApiService
import javax.inject.Inject

/**
 * 관심사 관련 데이터 관리 리포지토리
 */
class InterestRepository @Inject constructor(
    private val api: ApiService
) {
    /**
     * 서버에서 관심사 리스트를 받아옴 (토큰은 Interceptor에서 자동 추가)
     * @return Interest 목록, 실패 시 빈 리스트 반환
     */
    suspend fun getAllInterests(): List<Interest> {
        return try {
            val dtoList = api.getAllInterests()
            Log.d("InterestRepository", "서버에서 관심사 수신: ${dtoList.size}개, 데이터=$dtoList")
            dtoList.map { dto ->
                Interest(
                    interestId = dto.interestId,    // 서버 응답의 interestId
                    interestName = dto.interestName,

                )
            }
        } catch (e: Exception) {
            Log.e("InterestRepository", "관심사 목록 조회 실패", e)
            emptyList()
        }
    }
}
