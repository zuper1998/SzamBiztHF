package com.e.caffuserapp.Netwrok.Response

import com.e.caffuserapp.model.CaffDTO
import com.e.caffuserapp.model.Comment
import java.util.*


class GetAllCaffResponse {

    val id: UUID? = null

    val caff: CaffDTO? = null

    val username: String? = null

    val title: String? = null

    lateinit var comments: List<GetCommentResponse>

}