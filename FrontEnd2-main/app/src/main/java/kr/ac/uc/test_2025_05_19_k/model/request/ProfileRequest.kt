package kr.ac.uc.test_2025_05_19_k.model.request

import kr.ac.uc.test_2025_05_19_k.model.ProfileUpdateRequest
import kr.ac.uc.test_2025_05_19_k.network.ApiService

import javax.inject.Inject

class ProfileRequest @Inject constructor(
    private val api: ApiService
) {
    suspend fun getProfile() = api.getMyProfile()
    suspend fun updateProfile(request: ProfileUpdateRequest) = api.updateProfile(request)
}
