package com.example.healingyuk.data.model

data class Place(
    val id: Int,
    val name: String,
    val description: String,
    val short_description: String,
    val category: String,
    val image_url: String,
    val latitude: Double?,
    val longitude: Double?,
    val user_id: Int,
    val created_at: String
)
