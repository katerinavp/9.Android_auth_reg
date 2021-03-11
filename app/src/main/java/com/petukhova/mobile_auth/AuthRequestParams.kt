package com.petukhova.mobile_auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Данные для аутентификации
data class AuthRequestParams(val username: String, val password: String) {



}