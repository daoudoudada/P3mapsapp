package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class MyApp : Application() {
        companion object {
            lateinit var database: MySupabaseClient
        }
        override fun onCreate() {
            super.onCreate()
            database = MySupabaseClient(
                supabaseUrl = "https://dsfaxixjldkoywagxxtw.supabase.co",
                supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRzZmF4aXhqbGRrb3l3YWd4eHR3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc1OTcyMjEsImV4cCI6MjA2MzE3MzIyMX0.M7WtQC3Gp55ISGUpprp1aD5uhAk8eZ4AAd5JqKog9hE"
            )
        }

    }