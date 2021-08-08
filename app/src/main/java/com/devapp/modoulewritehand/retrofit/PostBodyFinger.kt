package com.devapp.modoulewritehand.retrofit

data class PostBodyFinger(
    val api_level: String,
    val app_version: Double,
    val device: String,
    val input_type: Int,
    val options: String,
    val requests: List<Request>
)