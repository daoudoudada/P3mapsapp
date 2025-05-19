package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.CameraScreen
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DetailMarkerScreen
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerListScreen
import com.example.mapsapp.viewmodels.MyViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun InternalNavigationWrapper(navController: NavHostController, modifier: Modifier) {
    val myViewModel : MyViewModel = viewModel()
    NavHost(navController, Destination.Home) {
        composable<Destination.Home> {
            MapsScreen(modifier,myViewModel) { navController.navigate(Destination.CreateMarker) }
        }
            composable<Destination.CreateMarker> {
                CreateMarkerScreen(
                    modifier = modifier,

                    onMarkerCreated = { navController.navigate(Destination.Home) },
                    myViewModel = myViewModel
                )
            }
        composable<Destination.Camera> {
            CameraScreen {navController.navigate(Destination.CreateMarker)}
            }


        composable<Destination.ListMarker> {
            MarkerListScreen { id -> navController.navigate(Destination.DetailMarker(id))
            }
        }

        composable<Destination.DetailMarker> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.DetailMarker>()
            DetailMarkerScreen(modifier,args.id) {  navController.navigate(Destination.ListMarker) {
                popUpTo<Destination.ListMarker> { inclusive = true }
            }}
        }

    }
}

