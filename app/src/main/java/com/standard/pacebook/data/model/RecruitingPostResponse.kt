package com.standard.pacebook.data.model

data class RecruitingPostResponse(
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val scheduled_time: String,
    val location_lat: String,
    val location_lng: String,
    val pace: Int,
    val distance_km: Int,
    val description: String
)