package com.standard.pacebook.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecruitingPost(
    val id: Int,
    val title: String,
    val scheduled_time: String,
    val location_lat: String,
    val location_lng: String,
    val pace: Int,
    val distance_km: Int,
    val description: String
) : Parcelable