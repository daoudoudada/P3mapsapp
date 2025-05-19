package com.example.mapsapp.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Model.Marcador
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class MyViewModel : ViewModel() {
    val database = MyApp.database


    private val _markerTitle = MutableLiveData<String>()
    val markerTitle = _markerTitle

    private val _marcadorList = MutableLiveData<List<Marcador>>()
    val markerList = _marcadorList

    private val _markerPosition = MutableLiveData<LatLng>()
    val markerPosition: LiveData<LatLng> = _markerPosition

    fun setMarkerPosition(lat: Double, lng: Double) {
        _markerPosition.value = LatLng(lat, lng)
    }

    fun setMarkerTitle(latLng: LatLng) {
        _markerPosition.value=latLng
    }



    fun insertMarker(
        userId: String?,
        title: String,
        description: String?,
        latitude: Double,
        longitude: Double,
        imageUrl: String? = null
    ) {
        val newMarker = Marcador(
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
                _marcadorList.value = markers
                Log.d("MyViewModel", "Markers retrieved: $markers")
            }
        }
    }
    private val _marker = MutableLiveData<Marcador>()
    val marker = _marker

    fun getMarkerById(markerId: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            val marker = database.getMarkerById(markerId)
            withContext(Dispatchers.Main) {
                _marker.value = marker
            }
        }
    }
    private val _fotodelmarcador= MutableLiveData<Bitmap>()
    val fotodelmarcador= _fotodelmarcador

    fun setfoto(bitmap: Bitmap){
        _fotodelmarcador.value=bitmap
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertMarkerWithImage(
        userId: String?,
        title: String,
        description: String?,
        latitude: Double,
        longitude: Double,
        image: Bitmap?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var imageUrl: String? = null
            image?.let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                imageUrl = database.uploadImage(stream.toByteArray())
            }
            database.insertMarker(
                Marcador(
                    user_id = userId,
                    title = title,
                    description = description,
                    latitude = latitude,
                    longitude = longitude,
                    image_url = imageUrl
                )
            )
            getAllMarkers()
        }
    }

        fun updateMarker(
            id: Int,
            title: String,
            description: String?,
            imageUrl: String?,
            image: Bitmap?
        ) {
            val stream = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageName = imageUrl?.removePrefix("https://dsfaxixjldkoywagxxtw.supabase.co/storage/v1/object/public/images/")
            CoroutineScope(Dispatchers.IO).launch {
                database.updateMarkerWithPhoto(
                    id = id,
                    title = title,
                    description = description,
                    imageName = imageName ?: "",
                    imageFile = stream.toByteArray()
                )
                getAllMarkers()
            }
        }



    }

