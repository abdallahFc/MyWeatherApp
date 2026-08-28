package com.example.myweather.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.R
import com.example.myweather.presentation.model.ThemeMode
import com.example.myweather.presentation.ui.PreviewSurface

@Composable
fun ThemeToggle(
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalIconButton(
        onClick = { onThemeModeChanged(themeMode.next()) },
        modifier = modifier.size(48.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        AnimatedContent(
            targetState = themeMode,
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
            label = "themeModeIcon",
        ) { mode ->
            Icon(
                painter = painterResource(mode.iconRes()),
                contentDescription = stringResource(mode.actionDescriptionRes()),
            )
        }
    }
}

private fun ThemeMode.iconRes(): Int = when (this) {
    ThemeMode.System -> R.drawable.ic_theme_system
    ThemeMode.Light -> R.drawable.ic_theme_light
    ThemeMode.Dark -> R.drawable.ic_theme_dark
}

private fun ThemeMode.actionDescriptionRes(): Int = when (this) {
    ThemeMode.System -> R.string.theme_switch_to_light
    ThemeMode.Light -> R.string.theme_switch_to_dark
    ThemeMode.Dark -> R.string.theme_switch_to_system
}

@Preview
@Composable
private fun ThemeTogglePreview() {
    PreviewSurface { EveryThemeMode() }
}

@Preview
@Composable
private fun ThemeToggleDarkPreview() {
    PreviewSurface(darkTheme = true) { EveryThemeMode() }
}

@Composable
private fun EveryThemeMode() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ThemeMode.entries.forEach { mode ->
            ThemeToggle(themeMode = mode, onThemeModeChanged = {})
        }
    }
}
