package com.example.myapplication.models

class User {
    private var userId: String? = null
    private var email: String? = null
    private var username: String? = null
    private var password: String? = null
    private var progress: Statistics? = null

    fun getUserId(): String {
        return userId!!
    }

    fun setUserId(userId: String?) {
        this.userId = userId
    }

    fun getEmail(): String {
        return email!!
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getUsername(): String {
        return username!!
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getPassword(): String {
        return password!!
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getProgress(): Statistics {
        return progress!!
    }

    fun setProgress(progress: Statistics?) {
        this.progress = progress
    }
}