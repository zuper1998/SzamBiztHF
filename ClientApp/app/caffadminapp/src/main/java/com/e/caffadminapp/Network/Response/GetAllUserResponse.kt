package com.e.caffadminapp.Network.Response

import android.provider.ContactsContract.CommonDataKinds.Email
import java.util.*


class GetAllUserResponse {

    private lateinit var id: UUID

    private lateinit var username: String

    private lateinit var email: String

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getId(): UUID {
        return id
    }

    fun setId(id: UUID) {
        this.id = id
    }

    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun GetAllUserResponse(id: UUID, username: String, email: String) {
        this.id = id
        this.username = username
        this.email = email
    }
}