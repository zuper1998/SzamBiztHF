package com.e.szambizthfapplibrary.network.Request


class SignUpRequest {
    private lateinit var username: String

    private lateinit var password: String

    private lateinit var email: String

    private lateinit var roles: Set<String>

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

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getRoles(): Set<String>{
        return roles
    }

    constructor(username: String, password: String, email: String) {
        this.username = username
        this.password = password
        this.email= email
    }

    constructor(username: String, password: String, email: String, role : String) {
        this.username = username
        this.password = password
        this.email= email
        roles = mutableSetOf(role)
    }
}