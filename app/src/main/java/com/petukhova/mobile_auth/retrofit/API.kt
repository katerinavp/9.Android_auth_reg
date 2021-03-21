package com.petukhova.mobile_auth.retrofit


import com.petukhova.mobile_auth.data.AuthRequestParams
import com.petukhova.mobile_auth.data.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    // URL запроса (без учета основного адреса)
    @POST("/api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("/api/v1/registration")
    suspend fun registration(@Body authRequestParams: AuthRequestParams): Response<Token>
}
