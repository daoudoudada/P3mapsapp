package com.example.mapsapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {

    @Serializable
    object Home: Destination()

    @Serializable
    object permision: Destination()

    @Serializable
    object CreateMarker: Destination()

    @Serializable
    object ListMarker: Destination()

    @Serializable
    object Login: Destination()

    @Serializable
    object  Register: Destination()

    @Serializable
    object Drawer: Destination()

    @Serializable
    object Settings: Destination()

    @Serializable
    object About: Destination()
}
