package com.e.caffadminapp.Network.Service

import com.e.caffadminapp.model.Log
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface LogService {
    @GET("/api/log")
    fun getAll(@Header("Authorization") token: String): Call<List<Log>>
}