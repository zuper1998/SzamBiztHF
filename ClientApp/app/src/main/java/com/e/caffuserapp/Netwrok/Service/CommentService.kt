package com.e.caffuserapp.Netwrok.Service

import com.e.szambizthfapplibrary.network.Response.GetCommentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface CommentService {

    @POST("/api/comment/addComment/{id}")
    fun addComment(/* Body-ba itt mi megy? */@Body text: RequestBody , @Header("Authorization") token: String, @Path("id") id: UUID): Call<Void>

    @GET("/api/comment/{id}/getComments")
    fun getCommentFromCaff(@Header("Authorization") token: String, @Path("id") id: UUID): Call<List<GetCommentResponse>>

}