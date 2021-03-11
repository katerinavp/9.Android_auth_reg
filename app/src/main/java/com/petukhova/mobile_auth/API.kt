package com.petukhova.mobile_auth


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {
    // URL запроса (без учета основного адреса)
    @POST("/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("/registration")
    suspend fun registration(@Body authRequestParams: AuthRequestParams): Response<Token>
}
