# Mercado Bitcoin Desafio

Cryptocurrency Exchange viewer app built with Android Jetpack Compose.

## Added Libraries

The following libraries were added beyond the default Android project template:

### Networking
- **Retrofit**: HTTP client for API communication
- **OkHttp Logging Interceptor**: Network request/response logging
- **Gson Converter**: JSON serialization/deserialization for Retrofit

### Image Loading
- **Coil Compose**: Async image loading for Compose
- **Coil Network OkHttp**: Network layer for Coil image loading

### Dependency Injection
- **Hilt**: Dependency injection framework
- **Hilt Navigation Compose**: Hilt integration with Compose Navigation
- **KSP (Kotlin Symbol Processing)**: Annotation processing for Hilt

### UI & Navigation
- **Navigation Compose**: Type-safe navigation for Compose
- **Lifecycle ViewModel Compose**: ViewModel integration with Compose
- **Google Fonts**: Custom font support for Compose
- **Core Splash Screen**: Modern splash screen implementation

### Data Management
- **Paging Runtime**: Pagination support for large datasets
- **Paging Compose**: Paging integration with Compose LazyColumn

### Testing
- **MockK**: Mocking framework for Kotlin
- **Coroutines Test**: Testing utilities for coroutines
- **Turbine**: Testing library for Flow
- **Arch Core Testing**: Testing utilities for Architecture Components

## Architecture Decisions

### MVVM (Model-View-ViewModel)
- **Separation of concerns**: UI logic separated from business logic and data layer
- **ViewModels** hold UI-related state and handle business logic
- **StateFlow/Flow** for reactive data streams between layers
- **Compose UI** acts as the View layer, observing ViewModel state

### Screen Events
- **Event-driven architecture**: UI interactions are modeled as sealed class events
- **Unidirectional data flow**: Events flow up from UI to ViewModel, state flows down
- **Clear intent modeling**: Each user action is explicitly defined as an event type
- **Testable interactions**: Events make user interactions easy to test and reason about

### Observability
- **Reactive state management**: StateFlow/Flow for observable state changes
- **State hoisting**: State is lifted up to ViewModels and flows down to Composables
- **Lifecycle awareness**: State automatically updates UI when data changes
- **Error handling**: Loading, success, and error states are modeled explicitly

### Repository Pattern
- **Data layer abstraction**: Repository acts as single source of truth for data
- **API integration**: Repositories handle network calls and data transformation
- **Dependency inversion**: Use cases depend on repository interfaces, not implementations
- **Caching strategy**: Repository manages data caching and refresh logic
