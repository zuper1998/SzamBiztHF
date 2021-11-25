package com.e.szambizthfapplibrary.network.Request


class UpdateUserRequest {

    private lateinit var email: String

    private lateinit var password: String

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }
}