package com.e.szambizthfapplibrary.network.Response

import java.util.*

class LoginResponse {
    private lateinit var id: UUID

    private lateinit var username: String

    private lateinit var email: String

    private lateinit var jwtToken: String

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

    fun getJwtToken(): String {
        return jwtToken
    }

    fun setJwtToken(jwtToken: String) {
        this.jwtToken = jwtToken
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun LoginResponse(id: UUID, username: String, email: String, jwtToken: String) {
        this.id = id
        this.username = username
        this.email = email
        this.jwtToken = jwtToken
    }
}