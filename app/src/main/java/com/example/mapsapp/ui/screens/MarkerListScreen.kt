package com.example.mapsapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Model.Marker
import com.example.mapsapp.viewmodels.MyViewModel
import java.util.UUID

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkerListScreen(navigateToDetail: (UUID) -> Unit) {
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
fun MarkerItem(marker: Marker, navigateToDetail: (UUID) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(width = 2.dp, color = Color.DarkGray)
            .clickable { navigateToDetail(marker.id) }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(marker.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            marker.description?.let {
                Text(text = it)
            }
            Text(text = "Lat: ${marker.latitude}, Lng: ${marker.longitude}")
        }
    }
}