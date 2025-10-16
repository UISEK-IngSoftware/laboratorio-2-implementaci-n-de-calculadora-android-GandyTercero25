package com.gandytercero.calculadora.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta de colores para el tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Orange,           // Color para los operadores
    onPrimary = White,
    secondary = MediumGray,     // Color para los números
    onSecondary = White,
    tertiary = LightGray,       // Color para las funciones (AC, C)
    onTertiary = DarkGray,      // Texto sobre los botones de funciones
    background = DarkGray,      // Fondo de la app
    onBackground = White,       // Texto principal (display)
    surface = DarkGray,         // Fondo de "superficies" como el display
    onSurface = White           // Texto sobre las superficies
)

// Paleta de colores para el tema claro (opcional)
private val LightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = White,
    secondary = LightKeypad,
    onSecondary = LightText,
    tertiary = LightGray,
    onTertiary = DarkGray,
    background = LightBackground,
    onBackground = LightText,
    surface = LightBackground,
    onSurface = LightText
)

@Composable
fun CalculadoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Deshabilitamos el color dinámico
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Cambiamos el color de la barra de estado para que coincida con el fondo
            window.statusBarColor = colorScheme.background.toArgb()
            // Ajustamos el color de los iconos de la barra de estado (reloj, batería, etc.)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}