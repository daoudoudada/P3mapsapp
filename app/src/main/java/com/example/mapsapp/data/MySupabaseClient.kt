package com.example.mapsapp.data


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mapsapp.data.Model.Marcador
import com.example.mapsapp.utils.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import com.example.mapsapp.BuildConfig
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MySupabaseClient() {
    lateinit var client: SupabaseClient
    lateinit var storage: Storage
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY
    constructor(supabaseUrl: String, supabaseKey: String): this(){
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
            install(Storage)

            install(Auth){
                autoLoadFromStorage = true
            }
        }
        storage = client.storage

    }
    suspend fun getAllMarkers(): List<Marcador> {
        return client.from("markers").select().decodeList<Marcador>()
    }

    suspend fun getMarkerById(id: Int?): Marcador {
        requireNotNull(id) { "El id no puede ser nulo" }
        return client.from("markers").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marcador>()
    }
    suspend fun insertMarker(marcador: Marcador){
        client.from("markers").insert(marcador)
    }
    suspend fun updateMarker(id: Int?, titulo: String, description: String?){
        requireNotNull(id) { "El id no puede ser nulo" }
        client.from("markers").update({
            set("title", titulo)
            set("description", description)
        }) { filter { eq("id", id) } }
    }
    suspend fun deleteStudent(id: String){
        client.from("Student").delete{ filter { eq("id", id) } }
    }

    suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthState {
        return try {
            client.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }
            val session = client.auth.currentSessionOrNull()
            if (session != null && session.accessToken != null) {
                AuthState.Authenticated
            } else {
                AuthState.Error("No se pudo crear la sesión")
            }
        } catch (e: Exception) {
            AuthState.Error(e.localizedMessage)
        }
    }
    suspend fun signInWithEmail(emailValue: String, passwordValue: String): AuthState {
        try {
            client.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }
    fun retrieveCurrentSession(): UserSession?{
        val session = client.auth.currentSessionOrNull()
        return session
    }

    fun refreshSession(): AuthState {
        try {
            client.auth.currentSessionOrNull()
            return AuthState.Authenticated
        } catch (e: Exception) {
            return AuthState.Error(e.localizedMessage)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imageName = storage.from("images").upload(
            path = "image_${fechaHoraActual.format(formato)}.png",
            data = imageFile
        )
        return buildImageUrl(imageFileName = imageName.path)
    }

    fun buildImageUrl(imageFileName: String) =
        "${this.supabaseUrl}/storage/v1/object/public/images/${imageFileName}"

    suspend fun updateMarkerWithPhoto(
        id: Int,
        title: String,
        description: String?,
        imageName: String,
        imageFile: ByteArray
    ) {
        val updatedImage = storage.from("images").update(path = imageName, data = imageFile)
        client.from("markers").update({
            set("title", title)
            set("description", description)
            set("image_url", buildImageUrl(imageFileName = updatedImage.path))
        }) {
            filter {
                eq("id", id)
            }
        }
    }
    suspend fun deleteImage(imageUrl: String) {
        val imgName = imageUrl.removePrefix("https://dsfaxixjldkoywagxxtw.supabase.co/storage/v1/object/public/images/")
        storage.from("images").delete(imgName)
    }

    suspend fun deleteMarker(id: Int) {
        client.from("markers").delete { filter { eq("id", id) } }
    }


}

