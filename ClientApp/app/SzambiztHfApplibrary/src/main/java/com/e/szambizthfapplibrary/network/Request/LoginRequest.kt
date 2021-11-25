package com.e.szambizthfapplibrary.network.Request


class LoginRequest {
    private lateinit var username: String

    private lateinit var password: String

    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    constructor(username: String, password: String) {
        this.password = password
        this.username = username
    }

    constructor()
}