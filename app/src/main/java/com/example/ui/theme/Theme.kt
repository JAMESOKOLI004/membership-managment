package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FitAdminLightColorScheme = lightColorScheme(
  primary = Charcoal900,
  onPrimary = Color.White,
  primaryContainer = Charcoal800,
  onPrimaryContainer = Color.White,
  secondary = ElectricLime500,
  onSecondary = Charcoal900,
  secondaryContainer = ElectricLime100,
  onSecondaryContainer = ElectricLime900,
  tertiary = AlertRed500,
  onTertiary = Color.White,
  tertiaryContainer = AlertRed100,
  onTertiaryContainer = AlertRed800,
  background = BackgroundLight,
  onBackground = TextPrimary,
  surface = SurfaceWhite,
  onSurface = TextPrimary,
  surfaceVariant = Color(0xFFF3F4F6),
  onSurfaceVariant = TextSecondary,
  outline = BorderLight,
  outlineVariant = BorderMedium,
  error = AlertRed500,
  onError = Color.White,
  errorContainer = AlertRed100,
  onErrorContainer = AlertRed800
)

private val FitAdminDarkColorScheme = darkColorScheme(
  primary = ElectricLime500,
  onPrimary = Charcoal900,
  primaryContainer = Charcoal800,
  onPrimaryContainer = Color.White,
  secondary = ElectricLime400,
  onSecondary = Charcoal900,
  secondaryContainer = Charcoal700,
  onSecondaryContainer = ElectricLime400,
  tertiary = AlertRed500,
  onTertiary = Color.White,
  background = Charcoal900,
  onBackground = Color.White,
  surface = Charcoal800,
  onSurface = Color.White,
  surfaceVariant = Charcoal700,
  onSurfaceVariant = Color(0xFFD1D5DB),
  outline = Charcoal600,
  outlineVariant = Charcoal700,
  error = AlertRed500,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Default to light mode for the #F9FAFB background + black/lime cards
  dynamicColor: Boolean = false, // Keep brand aesthetic strict
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) FitAdminDarkColorScheme else FitAdminLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
