package com.example.mapsapp.ui.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.mapsapp.viewmodels.MyViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen(
    modifier: Modifier = Modifier,
    myViewModel: MyViewModel,
    navigateToCreate: () -> Unit
) {
    val markerList by myViewModel.markerList.observeAsState(emptyList())
    val itb = LatLng(41.4534225, 2.1837151)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(itb, 17f)
    }

    LaunchedEffect(Unit) {
        myViewModel.getAllMarkers()
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLongClick = { latLng ->
            myViewModel.setMarkerPosition(latLng.latitude, latLng.longitude)
            navigateToCreate()
        }
    ) {
        // Marcador fijo de ITB
        Marker(
            state = MarkerState(position = itb),
            title = "ITB",
            snippet = "Institut Tecnològic de Barcelona"
        )

        // Marcadores desde la base de datos
        markerList.forEach { marcador ->
            Marker(
                state = MarkerState(position = LatLng(marcador.latitude, marcador.longitude)),
                title = marcador.title,
                snippet = marcador.description ?: ""
            )
        }
    }
}
