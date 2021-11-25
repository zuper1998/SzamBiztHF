package com.e.caffuserapp.model

import com.e.szambizthfapplibrary.network.Response.LoginResponse
import java.util.*

class UserData {
    companion object {
        private lateinit var id: UUID

        private lateinit var email: String

        private lateinit var userName: String

        private lateinit var token: String

        fun getId(): UUID {
            return id
        }

        fun setEmail(email: String) {
            this.email = email
        }

        fun getEmail(): String {
            return email
        }

        fun getUserName(): String {
            return userName
        }

        fun getToken(): String {
            return token
        }

        fun login(loginResponse: LoginResponse) {
            token = loginResponse.getJwtToken()
            userName = loginResponse.getUsername()
            email = loginResponse.getEmail()
            this.id = loginResponse.getId()
        }
    }
}