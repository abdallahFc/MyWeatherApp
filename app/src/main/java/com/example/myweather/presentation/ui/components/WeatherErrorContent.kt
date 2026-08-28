package com.example.myweather.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myweather.presentation.model.WeatherErrorAction
import com.example.myweather.presentation.model.WeatherErrorUiModel
import com.example.myweather.presentation.ui.PreviewCityNotFoundError
import com.example.myweather.presentation.ui.PreviewOfflineError
import com.example.myweather.presentation.ui.PreviewSurface

@Composable
fun WeatherErrorContent(
    error: WeatherErrorUiModel,
    onRetryClicked: () -> Unit,
    onEditSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WeatherMessage(
        iconRes = error.iconRes,
        iconTint = MaterialTheme.colorScheme.error,
        plateColor = MaterialTheme.colorScheme.errorContainer,
        title = stringResource(error.titleRes),
        message = stringResource(error.messageRes),
        modifier = modifier,
        actionLabel = stringResource(error.actionLabelRes),
        onActionClick = when (error.action) {
            WeatherErrorAction.Retry -> onRetryClicked
            WeatherErrorAction.EditSearch -> onEditSearchClicked
        },
    )
}

@Preview
@Composable
private fun WeatherErrorContentCityNotFoundPreview() {
    PreviewSurface {
        WeatherErrorContent(
            error = PreviewCityNotFoundError,
            onRetryClicked = {},
            onEditSearchClicked = {},
        )
    }
}

@Preview
@Composable
private fun WeatherErrorContentOfflineDarkPreview() {
    PreviewSurface(darkTheme = true) {
        WeatherErrorContent(
            error = PreviewOfflineError,
            onRetryClicked = {},
            onEditSearchClicked = {},
        )
    }
}
