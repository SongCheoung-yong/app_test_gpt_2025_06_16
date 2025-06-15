// ProfileRepository.kt
package kr.ac.uc.test_2025_05_19_k.repository

import kr.ac.uc.test_2025_05_19_k.model.ProfileUpdateRequest
import kr.ac.uc.test_2025_05_19_k.model.UserProfileResponse
import kr.ac.uc.test_2025_05_19_k.network.ApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // 싱글톤으로 사용하려면 붙이세요 (옵션)
class ProfileRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun updateProfile(profileRequest: ProfileUpdateRequest): Response<UserProfileResponse> {
        return apiService.updateProfile(profileRequest)
    }
}
