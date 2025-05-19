package com.example.mapsapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Model.Marcador
import com.example.mapsapp.viewmodels.MyViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun MarkerListScreen(navigateToDetail: (Int?) -> Unit) {
    val myViewModel = viewModel<MyViewModel>()
    val markerList by myViewModel.markerList.observeAsState(emptyList())
    myViewModel.getAllMarkers()
    LazyColumn {
        items(markerList) { marker ->
            MarkerItem(marker, navigateToDetail)
        }
    }
}

@Composable
fun MarkerItem(marcador: Marcador, navigateToDetail: (Int?) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(width = 2.dp, color = Color.DarkGray)
            .clickable { navigateToDetail(marcador.id) }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            marcador.image_url?.let { url ->
                val supabaseUrl = "https://dsfaxixjldkoywagxxtw.supabase.co"
                val fullUrl = if (url.startsWith("http")) url else supabaseUrl + url
                AsyncImage(
                    model = fullUrl,
                    contentDescription = "Foto del marcador",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(marcador.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                marcador.description?.let {
                    Text(text = it)
                }
                Text(text = "Lat: ${marcador.latitude}, Lng: ${marcador.longitude}")
            }
        }
    }
}