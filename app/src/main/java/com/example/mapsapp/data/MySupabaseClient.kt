package com.example.mapsapp.data

import com.example.mapsapp.data.Model.Marker
import com.example.mapsapp.utils.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import java.util.UUID

class MySupabaseClient() {
    lateinit var client: SupabaseClient
    constructor(supabaseUrl: String, supabaseKey: String): this(){
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {

            install(Postgrest)
            install(Auth){
                autoLoadFromStorage = true
            }
        }
    }
    suspend fun getAllMarkers(): List<Marker> {
        return client.from("markers").select().decodeList<Marker>()
    }

    suspend fun getStudent(id: UUID): Marker{
        return client.from("markers").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marker>()
}
    suspend fun insertMarker(marker: Marker){
        client.from("markers").insert(marker)
    }
    suspend fun updateStudent(id: UUID, titulo: String, mark: Double){
        client.from("markers").update({
            set("titulo", titulo)
            set("mark", mark)
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


}

