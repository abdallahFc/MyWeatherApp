# My Weather

A single-screen Android app: type a city, see the current temperature in °C, the condition
and a matching icon, in light or dark theme.

## Decisions

It's Jetpack Compose with Material 3, MVVM and a unidirectional flow — `WeatherViewModel`
owns one `WeatherUiState`, the UI renders it, and every user action travels back through
the single `WeatherInteractionListener` contract the ViewModel implements. The code splits
into `data` / `domain` / `presentation`, where the domain holds only plain models and the
repository contract, so neither Retrofit DTOs nor Compose types can cross that boundary.
Weather comes from [wttr.in](https://wttr.in) (`format=j1`, no API key) and the last
**successfully** loaded city is stored in Preferences DataStore, so reopening the app
reloads it automatically. I deliberately skipped a DI framework and use-case classes — with
one screen and one data source they add indirection without removing any — but kept
dependency inversion, wiring the graph by hand in a single composition root
(`AppContainer`). I chose Retrofit with Gson over kotlinx.serialization because wttr.in
serves its JSON as `text/plain` and Gson converts by return type without needing a
compiler plugin.

## One wttr.in gotcha worth knowing

An unknown city is **not** a 404. wttr.in answers with **HTTP 500** and a plain-text body
beginning `location not found`, so that body — not the status code — is what maps to the
"City not found" state. Its geocoder will also resolve near-nonsense input to a real place,
so a bad spelling can legitimately return weather for somewhere unexpected.

## How to run

No API key and no configuration needed.

```bash
./gradlew assembleDebug
```

Needs JDK 17+ and an Android SDK with API 37 (`androidx.core` 1.19 and `lifecycle` 2.11
both require compiling against it; `minSdk` stays at 24).

Unit tests cover the ViewModel's loading/success transition, the rapid-search race, blank
input, cold-start restore, and the mapping of network failures onto user-facing errors:

```bash
./gradlew testDebugUnitTest
```
