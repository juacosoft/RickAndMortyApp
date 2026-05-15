package co.martketing.rickandmortyapp.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = RickGreen,
    secondary = MortyYellow,
    tertiary = PortalTeal,
    background = OnBackground,
    surface = OnBackground,
)

private val DarkColorScheme = darkColorScheme(
    primary = RickGreen,
    secondary = MortyYellow,
    tertiary = PortalTeal,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = OnBackground,
    onSurface = OnSurface,
)

@Composable
fun RickAndMortyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = RickAndMortyTypography,
        content = content
    )
}
