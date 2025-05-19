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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun MarkerListScreen(
    modifier: Modifier,
    myViewModel: MyViewModel,
    navigateToDetail: (Int?) -> Unit
) {
    val markerList by myViewModel.markerList.observeAsState(emptyList())

        myViewModel.getAllMarkers()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(markerList) { marker ->
            MarkerItem(marker, navigateToDetail)
        }
    }
}

@Composable
fun MarkerItem(
    marcador: Marcador,
    navigateToDetail: (Int?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(marcador.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            marcador.image_url?.let { url ->
                val supabaseUrl = "https://dsfaxixjldkoywagxxtw.supabase.co"
                val fullUrl = if (url.startsWith("http")) url else supabaseUrl + url

                AsyncImage(
                    model = fullUrl,
                    contentDescription = "Foto del marcador",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = marcador.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                marcador.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lat: %.4f, Lng: %.4f".format(marcador.latitude, marcador.longitude),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

