package com.example.myweather.presentation.model

enum class ThemeMode {
    System,
    Light,
    Dark;

    fun next(): ThemeMode = entries[(ordinal + 1) % entries.size]
}
