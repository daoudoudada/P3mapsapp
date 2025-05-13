package com.example.mapsapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @kotlinx.serialization.Serializable
    object Home: Destination()

    @kotlinx.serialization.Serializable
    object Settings: Destination()

    @Serializable
    object About: Destination()
}
