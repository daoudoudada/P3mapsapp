package com.example.mapsapp.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mapsapp.viewmodels.MyViewModel

@Composable
fun DetailMarkerScreen(
    modifier: Modifier = Modifier,
    markerId: Int?,
    viewModel: MyViewModel,
    onMarkerUpdated: () -> Unit,
) {
    val marker by viewModel.marker.observeAsState()
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(marker) {
        marker?.let {
            title = it.title
            description = it.description ?: ""
        }
    }

    viewModel.getMarkerById(markerId)

    marker?.let { markerData ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Editar Marcador",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val supabaseUrl = "https://dsfaxixjldkoywagxxtw.supabase.co"
                    val fullUrl = if (markerData.image_url?.startsWith("http") == true)
                        markerData.image_url
                    else
                        supabaseUrl + markerData.image_url

                    AsyncImage(
                        model = fullUrl,
                        contentDescription = "Imagen del marcador",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Actualizar foto")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.updateMarker(
                        id = markerId ?: return@Button,
                        title = title,
                        description = description,
                        imageUrl = markerData.image_url,
                        image = viewModel.fotodelmarcador.value
                    )
                    onMarkerUpdated()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Guardar cambios")
            }
        }

        // Launchers y lógica para la cámara/galería
        val takePictureLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && imageUri.value != null) {
                    val stream = context.contentResolver.openInputStream(imageUri.value!!)
                    stream?.use {
                        val originalBitmap = BitmapFactory.decodeStream(it)
                        val ratio = originalBitmap.width.toFloat() / originalBitmap.height
                        val resized = Bitmap.createScaledBitmap(originalBitmap, 800, (800 / ratio).toInt(), true)
                        bitmap.value = resized
                        viewModel.setfoto(resized)
                    }
                }
            }

        val pickImageLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    imageUri.value = it
                    val stream = context.contentResolver.openInputStream(it)
                    val img = BitmapFactory.decodeStream(stream)
                    bitmap.value = img
                    viewModel.setfoto(img)
                }
            }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Selecciona una opción") },
                text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        val uri = createImageUri(context)
                        imageUri.value = uri
                        takePictureLauncher.launch(uri!!)
                    }) {
                        Text("Tomar Foto")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        pickImageLauncher.launch("image/*")
                    }) {
                        Text("Galería")
                    }
                }
            )
        }
    }
}

