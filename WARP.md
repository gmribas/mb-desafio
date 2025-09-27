# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview
MB Desafio - Cryptocurrency Exchange Android app built with Jetpack Compose, using MVVM architecture with Repository pattern, Hilt for dependency injection, and Retrofit for networking. The app displays cryptocurrency listings and detailed information using the CoinMarketCap API.

## Build and Development Commands

### Build the project
```bash
./gradlew build
```

### Build debug APK
```bash
./gradlew assembleDebug
```

### Build release APK
```bash
./gradlew assembleRelease
```

### Run all tests
```bash
./gradlew test
```

### Run a specific test class
```bash
./gradlew test --tests "com.gmribas.mb.ui.exchangedetails.ExchangeDetailsScreenViewModelTest"
```

### Run a specific test method
```bash
./gradlew test --tests "com.gmribas.mb.ui.exchange.ExchangeListScreenViewModelTest.testMethodName"
```

### Clean build
```bash
./gradlew clean
```

### Install app on connected device
```bash
./gradlew installDebug
```

## Architecture Overview

### Core Architecture Pattern
The app follows MVVM with unidirectional data flow:
- **ViewModels** (`ui/*/ViewModel.kt`) handle business logic and expose StateFlow for UI state
- **Events** (`ui/*/model/*Event.kt`) model user interactions as sealed classes
- **Screens** (`ui/*/Screen.kt`) observe ViewModel state and emit events
- **Repository layer** (`repository/`) abstracts data operations
- **Data sources** (`data/datasource/`) handle API communication

### Navigation Structure
Navigation is type-safe using Compose Navigation:
- Entry point: `MainActivity` with `@AndroidEntryPoint`
- Routes defined in `navigation/Screen.kt`:
  - `Splash` - Initial splash screen
  - `Exchange` - Main cryptocurrency list
  - `ExchangeDetails/{id}` - Cryptocurrency detail view with complete information

### Dependency Injection
Hilt modules provide dependencies:
- `NetworkModule` - Retrofit, OkHttp, CoinMarketCap API configuration
- `DataSourceModule` - Cryptocurrency and Exchange data source implementations
- `RepositoryModule` - Cryptocurrency and Exchange repository implementations

### Data Flow
1. UI emits events to ViewModel
2. ViewModel processes events and updates StateFlow
3. ViewModel calls UseCase or Repository methods
4. Repository coordinates DataSource calls and maps data
5. DataSource makes API calls via CoinMarketCapApi
6. Data flows back through Repository → UseCase → ViewModel → UI

### API Integration
- Base URL: `https://pro-api.coinmarketcap.com/`
- Authentication via `CoinMarketCapInterceptor` using API key from `local.properties`
- Endpoints:
  - `/v1/cryptocurrency/listings/latest` - Latest cryptocurrency listings
  - `/v1/cryptocurrency/info` - Detailed cryptocurrency information

### Pagination
Cryptocurrency list uses Paging 3 library:
- `CryptocurrencyPagingSource` handles paginated loading
- Page size: 20 items
- Loads cryptocurrency data sorted by market cap

## Project Configuration

### API Key Setup
Add CoinMarketCap API key to `local.properties`:
```
COINMARKETCAP_KEY=your_api_key_here
```

### Key Dependencies
- **UI**: Jetpack Compose, Material3, Navigation Compose
- **DI**: Hilt with KSP processor
- **Networking**: Retrofit, OkHttp, Gson
- **Image Loading**: Coil 3
- **Pagination**: Paging 3 with Compose integration
- **Testing**: MockK, Turbine, Coroutines Test
- **Splash Screen**: Core Splash Screen API

### Module Structure
- `app/` - Single module Android application
  - `src/main/java/com/gmribas/mb/`
    - `core/` - Core utilities, network config, extensions (DateTimeExtensions, CurrencyExtensions)
    - `data/` - API interfaces, data sources, models, paging sources
      - `api/` - CoinMarketCapApi interface
      - `datasource/` - Cryptocurrency and Exchange data sources
      - `model/` - API response models
      - `paging/` - CryptocurrencyPagingSource
    - `domain/` - Use cases (GetCryptocurrenciesUseCase, GetExchangeDetailsUseCase)
    - `repository/` - Repository implementations with mappers
    - `ui/` - Screens, ViewModels, components
      - `splash/` - Splash screen
      - `exchange/` - Cryptocurrency list screen
      - `exchangedetails/` - Cryptocurrency details screen
      - `common/` - Shared UI components
      - `theme/` - Material3 theming
    - `navigation/` - Navigation setup
  - `src/test/` - Unit tests for ViewModels, UseCases, Repositories, DataSources
  - `src/androidTest/` - UI tests

## Testing Approach
Tests follow the architecture layers:
- ViewModels tested with MockK and Turbine for Flow testing
- Use cases tested with mocked repositories
- Repositories tested with mocked data sources and mappers
- Data sources tested with mocked API responses
- Use `runTest` for coroutine testing with proper dispatcher handling

## Key Features
- **Cryptocurrency Listings**: Browse cryptocurrencies with real-time pricing
- **Detailed Information**: View comprehensive cryptocurrency details including description, website, platform info
- **Pull-to-Refresh**: Refresh cryptocurrency data
- **Locale-aware Formatting**: Dates and numbers formatted based on device locale
- **Theme Support**: Light and dark theme with custom card backgrounds
- **Error Handling**: Graceful error states with retry options
- **Image Caching**: Efficient cryptocurrency logo loading with Coil
