package co.martketing.rickandmortyapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.martketing.rickandmortyapp.presentation.characters.CharactersScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Characters.route
    ) {
        composable(route = AppRoute.Characters.route) {
            CharactersScreen()
        }
    }
}
