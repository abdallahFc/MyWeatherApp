package com.example.myweather.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.R
import com.example.myweather.presentation.ui.PreviewSurface

@Composable
fun WeatherMessage(
    @DrawableRes iconRes: Int,
    iconTint: Color,
    plateColor: Color,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Surface(shape = CircleShape, color = plateColor) {
            Box(Modifier.size(88.dp), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(40.dp),
                )
            }
        }
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 264.dp),
        )
        if (actionLabel != null && onActionClick != null) {
            OutlinedButton(
                onClick = onActionClick,
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(text = actionLabel, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview
@Composable
private fun WeatherMessagePreview() {
    PreviewSurface {
        WeatherMessage(
            iconRes = R.drawable.ic_weather_partly_cloudy,
            iconTint = MaterialTheme.colorScheme.primary,
            plateColor = MaterialTheme.colorScheme.surfaceContainer,
            title = stringResource(R.string.weather_empty_title),
            message = stringResource(R.string.weather_empty_message),
        )
    }
}

@Preview
@Composable
private fun WeatherMessageWithActionPreview() {
    PreviewSurface {
        WeatherMessage(
            iconRes = R.drawable.ic_cloud_off,
            iconTint = MaterialTheme.colorScheme.error,
            plateColor = MaterialTheme.colorScheme.errorContainer,
            title = stringResource(R.string.error_server_title),
            message = stringResource(R.string.error_server_message),
            actionLabel = stringResource(R.string.action_try_again),
            onActionClick = {},
        )
    }
}
