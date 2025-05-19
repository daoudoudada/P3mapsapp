package com.example.mapsapp.data.Model

import kotlinx.datetime.Instant
import java.util.UUID

data class Marker(
    val id: UUID = UUID.randomUUID(),
    val user_id: UUID,
    val title: String,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val image_url: String? = null,
    val created_at: Instant? = null,
    val updated_at: Instant? = null
)