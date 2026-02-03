# MailRaven Mobile App

A Kotlin Multiplatform (KMP) mobile email client for the MailRaven server. Built with Compose Multiplatform, Ktor, and Voyager.

## Features

- **Authentication**: Login with email/password. Persistent session.
- **Inbox**: View list of received emails.
- **Read**: View full email content.
- **Compose**: Send new emails to other users.

## Tech Stack

- **Kotlin Multiplatform**: Shared logic for Android (and iOS/Desktop in future).
- **Compose Multiplatform**: Shared UI across platforms.
- **Ktor Client**: Networking and API interaction.
- **Voyager**: Navigation.
- **Koin**: Dependency Injection.
- **Multiplatform Settings**: Key-Value storage (Token persistence).
- **Kotlinx Serialization**: JSON parsing.

## Setup & Running

### Prerequisites

- **MailRaven Server**: Ensure the Go server is running locally on port `8080`.
  - Android assumes `http://10.0.2.2:8080` (Emulator localhost).
  - iOS/Desktop assumes `http://localhost:8080`.

### Android

Build and run the app on an Android Emulator or Device:

```bash
./gradlew :composeApp:installDebug
```

### Testing

Run Unit Tests (Auth, Message Repositories, ViewModels):

```bash
./gradlew :composeApp:testDebugUnitTest
```

## Project Structure

- `composeApp/src/commonMain`: Shared Code (UI, ViewModels, Repositories, Models).
- `composeApp/src/androidMain`: Android-specific implementations.
- `composeApp/src/iosMain`: iOS-specific implementations.

## CI/CD

GitHub Actions workflow is configured in `.github/workflows/android.yml` to run tests and build on push.
