package kr.ac.uc.test_2025_05_19_k.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("oauth/code") // 예: 서버에서 이 엔드포인트를 열어둔 경우
    fun sendAuthCode(@Field("code") code: String): Call<ResponseBody>
}