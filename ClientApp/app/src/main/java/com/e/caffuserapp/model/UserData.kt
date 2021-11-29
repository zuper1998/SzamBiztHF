package com.e.caffuserapp.model

import com.e.szambizthfapplibrary.network.Response.GetAllCaffResponse
import com.e.szambizthfapplibrary.network.Response.LoginResponse
import java.util.*
import kotlin.collections.ArrayList

class UserData {
    companion object {
        private lateinit var id: UUID

        private lateinit var email: String

        private lateinit var userName: String

        private lateinit var token: String

        private lateinit var selectedCaff: GetAllCaffResponse

        private var caffsCache: ArrayList<GetAllCaffResponse> = ArrayList()

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

        fun setCaffCache(caff: ArrayList<GetAllCaffResponse>) {
            caffsCache = caff
        }

        fun getCaffCache(): ArrayList<GetAllCaffResponse> {
            return caffsCache
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