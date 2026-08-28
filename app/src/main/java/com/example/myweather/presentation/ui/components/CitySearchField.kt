package com.example.myweather.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.R
import com.example.myweather.presentation.ui.PreviewSurface

@Composable
fun CitySearchField(
    city: String,
    showBlankCityError: Boolean,
    focusRequester: FocusRequester,
    onCityChanged: (String) -> Unit,
    onClearCityClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = onCityChanged,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = { Text(stringResource(R.string.search_city_hint)) },
                singleLine = true,
                isError = showBlankCityError,
                shape = MaterialTheme.shapes.medium,
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_search), contentDescription = null)
                },
                trailingIcon = {
                    if (city.isNotEmpty()) {
                        IconButton(onClick = onClearCityClicked) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close),
                                contentDescription = stringResource(R.string.search_city_clear),
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    errorContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                ),
            )
            FilledIconButton(
                onClick = onSearchClicked,
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = if (city.isBlank()) {
                    IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    IconButtonDefaults.filledIconButtonColors()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_forward),
                    contentDescription = stringResource(R.string.search_city_action),
                )
            }
        }

        AnimatedVisibility(visible = showBlankCityError) {
            InlineErrorMessage(
                message = stringResource(R.string.search_city_blank_error),
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
private fun InlineErrorMessage(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = message,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Preview
@Composable
private fun CitySearchFieldPreview() {
    PreviewSurface {
        CitySearchField(
            city = "Cairo",
            showBlankCityError = false,
            focusRequester = remember { FocusRequester() },
            onCityChanged = {},
            onClearCityClicked = {},
            onSearchClicked = {},
        )
    }
}

@Preview
@Composable
private fun CitySearchFieldBlankErrorPreview() {
    PreviewSurface {
        CitySearchField(
            city = "",
            showBlankCityError = true,
            focusRequester = remember { FocusRequester() },
            onCityChanged = {},
            onClearCityClicked = {},
            onSearchClicked = {},
        )
    }
}
