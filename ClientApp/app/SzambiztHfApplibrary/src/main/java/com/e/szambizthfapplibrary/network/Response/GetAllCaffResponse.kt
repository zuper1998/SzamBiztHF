package com.e.szambizthfapplibrary.network.Response

import com.e.szambizthfapplibrary.model.CaffDTO
import java.io.Serializable
import java.util.*

class GetAllCaffResponse {

    val id: UUID? = null

    val caff: CaffDTO? = null

    val username: String? = null

    var title: String? = null

    lateinit var comments: List<GetCommentResponse>

}