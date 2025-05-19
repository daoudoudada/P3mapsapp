package com.example.twonavigations.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.LoginScreen
import com.example.mapsapp.ui.screens.PermissionCamera
import com.example.mapsapp.ui.screens.PermissionScreen
import com.example.mapsapp.ui.screens.RegisterScreen


@Composable
fun ExternalNavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Destination.Login) {
        composable<Destination.Login> {
            LoginScreen(
                navigatetoregister = { navController.navigate(Destination.Register) },
            ) { navController.navigate(Destination.permision) }
        }
        composable<Destination.Register> {
            RegisterScreen{ navController.navigate(Destination.permision) }
        }
        composable<Destination.permision> {
            PermissionScreen{ navController.navigate(Destination.permisionCamera)}
        }
        composable<Destination.permisionCamera> {
            PermissionCamera{ navController.navigate(Destination.Drawer)}
        }
        composable<Destination.Drawer> {
                DrawerScreen {
                    navController.navigate(Destination.Login) {
                        popUpTo<Destination.Login> { inclusive = true }
                    }
                }
            }
        }
    }