package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mapsapp.viewmodels.MyViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun DetailMarkerScreen(
    modifier: Modifier,
    markerId: Int?,
    onMarkerUpdated: () -> Unit
) {
    val viewModel = viewModel<MyViewModel>()
    val marker by viewModel.marker.observeAsState()
    viewModel.getMarkerById(markerId)
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(marker) {
        marker?.let {
            title = it.title
            description = it.description ?: ""
        }
    }


    marker?.let {
        Column(modifier.padding(16.dp)) {
            Text("Detalle del Marcador")
            Spacer(modifier = Modifier.height(8.dp))
            it.image_url?.let { url ->
                val supabaseUrl = "https://dsfaxixjldkoywagxxtw.supabase.co"
                val fullUrl =
                    if (it.image_url?.startsWith("http") == true) it.image_url else supabaseUrl + it.image_url

                Log.d("DetailMarkerScreen", "Image URL: $url")
                AsyncImage(
                    model = fullUrl,
                    contentDescription = "Foto del marcador",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { showDialog = true }){
                    Text("actualizar foto")
                }

            }
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.updateMarker(
                    id = markerId ?: return@Button,
                    title = title,
                    description = description,
                    imageUrl = marker?.image_url,
                    image = viewModel.fotodelmarcador.value // o el Bitmap que quieras subir
                )
                onMarkerUpdated()
            }) {
                Text("Actualizar")
            }
        }

    }
}