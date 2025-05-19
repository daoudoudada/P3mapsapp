package com.example.mapsapp.data.Model

import android.graphics.Bitmap
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Marcador(
    val id: Int? =null,
    val user_id: String?,
    val title: String,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val image_url: String ? = null,
    val created_at: Instant? = null,
    val updated_at: Instant? = null
)