# MailRaven — Mobile Email Client

> A Kotlin Multiplatform email client for the MailRaven server, built with Compose Multiplatform.

---

## What Is This?

MailRaven is a cross-platform mobile email client that connects to the [MailRaven backend server](docs/api/API.md). It shares all business logic and UI across platforms through Kotlin Multiplatform, meaning one codebase targets Android today — with iOS ready to follow.

---

## Features

| Feature | Description |
|---|---|
| **Login** | Email/password authentication with persistent session |
| **Inbox** | View and browse received emails |
| **Read** | Open and read full email content |
| **Compose** | Write and send emails to other users |
| **Profile** | View your account profile |
| **Settings** | App preferences and configuration |
| **Theming** | Light (Cloud) and Dark (Midnight) theme support |

---

## Tech Stack

| Layer | Library |
|---|---|
| **UI** | [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) |
| **Navigation** | [Voyager](https://voyager.adriel.cafe/) |
| **Networking** | [Ktor Client](https://ktor.io/docs/client-create-new-application.html) |
| **Dependency Injection** | [Koin](https://insert-koin.io/) |
| **Serialization** | [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) |
| **Session Storage** | [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) |
| **Shared Logic** | [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) |

---

## Project Structure

```
composeApp/
  src/
    commonMain/       # Shared code: UI, ViewModels, Repositories, Models
      screens/
        login/        # Login screen + ScreenModel
        inbox/        # Inbox screen + ScreenModel
        detail/       # Message detail screen
        compose/      # Compose email screen
        profile/      # Profile screen
        settings/     # Settings screen
      repository/     # AuthRepository, MessageRepository
      model/          # Data models
      ui/theme/       # Colors, Theme (Light/Dark)
      di/             # Koin AppModule
    androidMain/      # Android-specific: Platform impl, base URL
    iosMain/          # iOS-specific: Platform impl, base URL
iosApp/               # iOS application entry point
```

---

## Getting Started

### Prerequisites

1. **JDK 17+** and **Android Studio** (Hedgehog or newer)
2. **MailRaven backend server** running locally on port `8080`
   - Clone and run the Go server separately
   - Android emulator connects via `http://10.0.2.2:8080`
   - iOS/Desktop connects via `http://localhost:8080`

### Run on Android

```bash
# Install on a connected device or running emulator
./gradlew :composeApp:installDebug
```

### Run Tests

```bash
# Unit tests for Auth, Message Repositories, and ViewModels
./gradlew :composeApp:testDebugUnitTest
```

---

## CI/CD

GitHub Actions is configured at `.github/workflows/android.yml` and runs on every push:
- Executes the unit test suite
- Builds the debug APK

---

## Documentation

Full project documentation lives in the [`docs/`](docs/README.md) folder:

- [API Specification](docs/api/API.md) — REST endpoints for client developers
- [Architecture Overview](docs/architecture/ARCHITECTURE.md) — System design and decisions
- [Client Developer Handover](docs/development/CLIENT_DEVELOPER_HANDOVER.md) — Onboarding guide
- [Production Guide](docs/guides/PRODUCTION.md) — Deployment, Docker, and database setup
