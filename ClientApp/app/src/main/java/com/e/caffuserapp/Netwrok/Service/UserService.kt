package com.e.caffuserapp.Netwrok.Service

import com.e.szambizthfapplibrary.network.Request.LoginRequest
import com.e.szambizthfapplibrary.network.Request.SignUpRequest
import com.e.szambizthfapplibrary.network.Request.UpdateUserRequest
import com.e.szambizthfapplibrary.network.Response.LoginResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface UserService {
    @POST("api/user/login/user")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/user/registration")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<Void>

    @PUT("api/user/userUpdate/{id}")
    fun updateAccount(@Body updateUserRequest: UpdateUserRequest, @Header("Authorization") token: String, @Path("id") id: UUID): Call<Void>
}