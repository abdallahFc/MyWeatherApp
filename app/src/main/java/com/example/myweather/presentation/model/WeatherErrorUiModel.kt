package com.example.myweather.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class WeatherErrorUiModel(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val titleRes: Int,
    @param:StringRes val messageRes: Int,
    @param:StringRes val actionLabelRes: Int,
    val action: WeatherErrorAction,
)

enum class WeatherErrorAction {

    Retry,
    EditSearch,
}
