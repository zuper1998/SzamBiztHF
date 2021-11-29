package com.e.caffuserapp.model

import com.e.caffuserapp.Netwrok.Response.GetAllCaffResponse
import com.e.szambizthfapplibrary.network.Response.LoginResponse
import java.util.*

class UserData {
    companion object {
        private lateinit var id: UUID

        private lateinit var email: String

        private lateinit var userName: String

        private lateinit var token: String

        private lateinit var selectedCaff: GetAllCaffResponse

        fun getId(): UUID {
            return id
        }

        fun setEmail(email: String) {
            this.email = email
        }

        fun getEmail(): String {
            return email
        }

        fun setSelectedCaff(caff: GetAllCaffResponse) {
            selectedCaff = caff
        }

        fun getSelectedCaff(): GetAllCaffResponse {
            return selectedCaff
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