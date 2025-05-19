package com.example.mapsapp.ui.navigation

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed class Destination {

    @Serializable
    object Home: Destination()

    @Serializable
    object permision: Destination()

    @Serializable
    object permisionCamera: Destination()

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
    object Camera: Destination()

    @Serializable
    data class DetailMarker (val id:Int?): Destination()

    @Serializable
    object About: Destination()
}
