package com.example.twonavigations.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.LoginScreen
import com.example.mapsapp.ui.screens.PermissionCamera
import com.example.mapsapp.ui.screens.PermissionScreen
import com.example.mapsapp.ui.screens.RegisterScreen
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.AuthViewModelFactory
import com.example.mapsapp.viewmodels.MyViewModel


@Composable
fun ExternalNavigationWrapper() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))
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