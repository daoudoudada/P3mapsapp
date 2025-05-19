package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Model.Marker
import com.example.mapsapp.data.MySupabaseClient
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID


class MyViewModel : ViewModel() {
    val database = MyApp.database


    private val _markerTitle = MutableLiveData<String>()
    val markerTitle = _markerTitle

    private val _markerList = MutableLiveData<List<Marker>>()
    val markerList = _markerList
    private val _markerPosition: MutableLiveData<LatLng> = MutableLiveData<LatLng>()
    val markerPosition = _markerPosition



    fun setMarkerTitle(latLng: LatLng) {
        _markerPosition.value=latLng
    }



    fun insertMarker(
        userId: UUID,
        title: String,
        description: String?,
        latitude: Double,
        longitude: Double,
        imageUrl: String? = null
    ) {
        val newMarker = Marker(
            user_id = userId,
            title = title,
            description = description,
            latitude = latitude,
            longitude = longitude,
            image_url = imageUrl
        )
        CoroutineScope(Dispatchers.IO).launch {
            database.insertMarker(newMarker)
            getAllMarkers()
        }
    }

    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val markers = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markerList.value = markers
            }
        }
    }



}