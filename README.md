# Compose Desktop Weather App

> Based on the [How to Build an MVI Clean Code Weather App](https://www.youtube.com/watch?v=eAbKK7JNxCE) tutorial
> by [Philipp Lackner](https://www.youtube.com/@PhilippLackner). The Weather domain model is heavily based on
> [his original implementation](https://github.com/philipplackner/WeatherApp).

This repository contains an implementation of a small weather forecast application using functional style, as
described in [Arrow's design section](https://arrow-kt.io/learn/design/) and the book [_Functional Ideas for
the Curious Kotliner_](https://leanpub.com/fp-ideas-kotlin).

The application uses [Open-Meteo](https://open-meteo.com/) to gather forecast data, following the [original 
tutorial](https://www.youtube.com/watch?v=eAbKK7JNxCE). [GeoIP2](https://www.maxmind.com/en/geoip2-city) is
used to map IPs to locations, since we don't use location services.

## Compose Desktop

The application is implemented in [Compose Multiplatform Desktop](https://www.jetbrains.com/lp/compose-multiplatform/)
instead of Android. The main reason is being able to use experimental Kotlin features, which are only available
in the JVM back-end. Furthermore, it makes it possible for everybody to check the application, even if they don't
own an Android phone nor want to download a simulator.

## State as sealed interface

The [original tutorial](https://www.youtube.com/watch?v=eAbKK7JNxCE) uses a class with nullable fields to represent
the different states of the application (loading, error, success).

```kotlin
data class WeatherState(val isLoading: Boolean, val weatherInfo: WeatherInfo?, val error: String?)
```

Our implementation uses [sealed interfaces](https://kotlinlang.org/docs/sealed-classes.html) instead.
Each state gets its own type, making it [impossible to represent invalid states](https://arrow-kt.io/learn/design/domain-modeling/),

```kotlin
sealed interface WeatherState {
    data object Loading : WeatherState
    data class Error(val error: String) : WeatherState
    data class Ok(val place: String?, val weatherInfo: WeatherInfo) : WeatherState
}
```

## Context receivers

Our implementation doesn't use dependency injection framework, as opposed to most Android applications, which use
[Hilt](https://developer.android.com/training/dependency-injection/hilt-android). Instead, the dependencies are
represented as [context receivers](https://github.com/Kotlin/KEEP/blob/master/proposals/context-receivers.md),

```kotlin
context(WeatherRepository, LocationTracker)
class WeatherViewModel { /* implementation */ }
```

The actual injection of dependencies is performed manually in the `Main` file,

```kotlin
suspend fun <A> injectDependencies(
    block: context(WeatherRepository, LocationTracker) () -> A
): A = resourceScope {
    val weather: WeatherRepository = WeatherRepositoryImpl(autoCloseable { WeatherApi() })
    val location: LocationTracker = autoCloseable { LocationTrackerImpl() }
    block(weather, location)
}
```

Another advantage of this approach, apart from the speed gains at both compile and run time, is that resources
are managed correctly using [Arrow's `resourceScope`](https://arrow-kt.io/learn/coroutines/resource-safety/).
This is often a convoluted task when using dependency injection frameworks -- when are instances actually created
and disposed -- whereas here everything is explicit.

### Lifecycle as `CoroutineScope` context

Jetpack Compose encourages to keep the activity state in a
[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel). One of the main benefits of
this approach is that ViewModels are lifecycle-aware. For example, if you launch a concurrent coroutine and the
activity is then closed, the coroutine is automatically cancelled.

This ability comes in a great deal from the
[structured concurrency](https://kotlinlang.org/docs/coroutines-basics.html#structured-concurrency)
guarantees from Kotlin's coroutines. If you capture a `CoroutineScope`, you can launch new coroutines tied to
the lifecycle of that scope. This is exactly what we do in our ViewModel,

```kotlin
context(/* others */, CoroutineScope)
class WeatherViewModel { /* implementation */ }
```

In our case we want to tie the lifecycle of the ViewModel to that of the entire application. This is done by
calling `rememberCoroutineScope` inside the `application` builder, and passing the obtained scope to
`WeatherViewModel`.

## Arrow DSLs

We've already mentioned that [`resourceScope`](https://arrow-kt.io/learn/coroutines/resource-safety/) is used
to correctly manage resource acquisition and disposal. This is one of Arrow's DSLs, each of them providing
additional features within a certain scope. The other one used heavily within this application are
[typed errors](https://arrow-kt.io/learn/typed-errors/working-with-typed-errors/).

The implementation of `LocationTracker`, found in `data.location.LocationTrackerImpl`, showcases how the DSLs
can be used and combined.
