# My Weather

A focused, single-screen Android application for checking the current weather in any city.
It fetches live data from [wttr.in](https://wttr.in), presents explicit loading and error
states, and restores the last successfully searched city when the app is opened again.

## Features

- Search for current weather by city name.
- View the resolved city, temperature in Celsius, weather condition, and a matching visual.
- See optional details including feels-like temperature, humidity, and wind speed.
- Restore the last successfully loaded city with Preferences DataStore.
- Handle blank input, no connection, unknown cities, server failures, and malformed responses.
- Cancel an older request when a newer city is submitted.
- Follow the system theme or switch between light and dark modes.
- English and Arabic resources, including automatic RTL layout when the device uses Arabic.
- Accessible labels for interactive controls and temperature output.

## Architecture

The project uses MVVM, unidirectional data flow, and a lightweight Clean Architecture
separation:

```text
UI event -> WeatherViewModel -> WeatherRepository contract
                                      ^
                                      |
                          WeatherRepositoryImpl
                           /                  \
                wttr.in remote source    Preferences DataStore

API DTO -> Data mapper -> Domain model -> UI mapper -> WeatherUiState -> Compose UI
```

```text
com.example.myweather
├── domain          Plain models, typed failures, and repository contract
├── data            Remote/local sources, DTOs, mapping, and repository implementation
├── presentation    ViewModel, UI state/models, interaction contract, and Compose UI
├── di              Manual dependency graph and composition root
├── MainActivity.kt
└── MyWeatherApplication.kt
```

The `domain` package has no dependency on Android, Retrofit, DataStore, or Compose. Both the
data and presentation layers depend inward on its models and repository contract.

## Key decisions

`WeatherViewModel` owns a single read-only `WeatherUiState`, while the Compose UI renders that
state and sends user actions through `WeatherInteractionListener`. Transport DTOs and UI models
are kept separate from `Weather` so API details and Android resources do not cross the domain
boundary. Only a successful search updates the city stored in DataStore, preventing invalid input
from replacing the last usable city. Dependencies are wired manually in `AppContainer` because
the app has one screen and one repository, while constructor injection keeps the graph testable.
Use-case classes and a DI framework were intentionally omitted at this scope; they can be added
when reusable business workflows or additional features justify the extra indirection.

## Technology

- Kotlin and coroutines
- Jetpack Compose with Material 3
- Android ViewModel and StateFlow
- Retrofit, OkHttp, and Gson
- Preferences DataStore
- JUnit and kotlinx-coroutines-test

## Weather API

The app calls:

```text
https://wttr.in/{city}?format=j1
```

No API key or local configuration is required. One provider-specific behavior is handled in the
remote data source: an unknown city is returned as HTTP 500 with a plain-text body containing
`location not found`, so the body is used to distinguish `CityNotFound` from a general server
failure.

## Run locally

Prerequisites:

- JDK 17 or newer
- Android SDK with API 37 installed
- Android Studio or the Gradle wrapper

Build the debug APK:

```bash
./gradlew assembleDebug
```

The generated APK is available at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Verification

Run unit tests, Android Lint, and a debug build:

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug
```

The unit tests cover ViewModel state transitions, blank input, cold-start restoration,
rapid-search cancellation, DTO-to-domain mapping, persistence behavior, and user-facing network
failure classification.

## Localization

Android selects the appropriate resources from the device locale. English is the default, while
Arabic strings live in `values-ar` and use the application's existing RTL support. City names are
displayed as resolved by the weather provider; normalized weather categories and application UI
messages are localized by the app.
