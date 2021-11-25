package com.e.caffadminapp.Network.Service

import com.e.caffadminapp.Network.Response.GetAllUserResponse
import com.e.szambizthfapplibrary.network.Request.LoginRequest
import com.e.szambizthfapplibrary.network.Request.SignUpRequest
import com.e.szambizthfapplibrary.network.Request.UpdateUserRequest
import com.e.szambizthfapplibrary.network.Response.LoginResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface UserService {
    @POST("api/user/login/admin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/user/adminAddUser")
    fun addUser(@Body singUpRequest: SignUpRequest, @Header("Authorization") token: String): Call<Void>

    @DELETE("api/user/{id}")
    fun deleteUser(@Header("Authorization") token: String, @Path("id") id:UUID): Call<Void>

    @GET("api/user")
    fun getAll(@Header("Authorization") token: String): Call<List<GetAllUserResponse>>

    @PUT("api/user/adminUpdate/{id}")
    fun updateUser(@Body updateUserRequest: UpdateUserRequest, @Header("Authorization") token: String, @Path("id") id:UUID): Call<Void>

    @PUT("api/user/userUpdate/{id}")
    fun updateAccount(@Body updateUserRequest: UpdateUserRequest, @Header("Authorization") token: String, @Path("id") id:UUID): Call<Void>

}