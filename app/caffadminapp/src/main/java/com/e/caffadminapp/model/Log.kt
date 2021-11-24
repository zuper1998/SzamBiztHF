package com.e.caffadminapp.model

class Log {

    private lateinit var event: String

    private lateinit var date: String

    fun getEvent(): String {
        return event
    }

    fun setEvent(event: String) {
        this.event = event
    }

    fun getDate(): String {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }

    constructor(event: String, date: String) {
        this.event = event
        this.date = date
    }
}