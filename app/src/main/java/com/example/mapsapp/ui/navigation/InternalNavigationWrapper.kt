package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.screens.LoginScreen
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.PermissionScreen
import com.example.mapsapp.ui.screens.RegisterScreen

@Composable
fun InternalNavigationWrapper(navController:NavController, modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController, Destination.Home) {
        composable<Destination.Home> {
            MapsScreen {
                navController.navigate(Destination.CreateMarker) {
                }
            }
        }


        }

    }

