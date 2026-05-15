package co.martketing.rickandmortyapp.presentation.navigation

sealed class AppRoute(val route: String) {
    object Characters : AppRoute("characters")
}
