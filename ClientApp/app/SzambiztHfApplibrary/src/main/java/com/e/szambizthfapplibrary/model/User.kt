package com.e.szambizthfapplibrary.model

import java.util.*

class User {

    private var id:UUID ?= null

    private lateinit var username:String

    private lateinit var password:String

    private lateinit var email:String

    constructor(username:String, password:String, email: String) {
        this.username = username
        this.password = password
        this.email = email
    }

    constructor(username:String, password:String) {
        this.username = username
        this.password = password
        this.email = ""
    }

    constructor(id:UUID, username: String, email: String) {
        this.id= id
        this.username = username
        this.email = email
    }

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
    fun getId(): UUID {
        return id!!
    }

}