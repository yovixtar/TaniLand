package com.ykstar.bangkit.taniland.models

data class UserModel(
    val id: String,
    val username: String,
    val email: String,
    val photo: String,
    val lahan: List<LahanModel>,
    val premium: Boolean,
    val token: String,
    val lasted_login: String
)

data class AuthResponse(
    val error: Boolean,
    val message: String,
    val data: UserModel
)