package com.e.caffuserapp.Netwrok.Service

import com.e.szambizthfapplibrary.network.Request.UpdateCaffRequest
import com.e.szambizthfapplibrary.network.Response.GetAllCaffResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface CaffService {

    @Multipart
    @POST("/api/caff/addCaff")
    fun addCaff(/* Body-ba itt mi megy? */@Part caffFile: MultipartBody.Part, @Part("title") title: RequestBody,  @Header("Authorization") token: String): Call<Void>

    @GET("api/caff/getAllCaff")
    fun getAll(@Header("Authorization") token: String): Call<List<GetAllCaffResponse>>

    @PUT("api/caff/{id}")
    fun updateCaff(@Body updateCaffRequest: UpdateCaffRequest, @Header("Authorization") token: String, @Path("id") id: UUID): Call<Void>

}