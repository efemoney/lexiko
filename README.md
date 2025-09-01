# Lexiko

A Kotlin Multiplatform word game.

## Features

*   **Cross-platform:** Runs on Android and Desktop.
*   **Modular Architecture:** The project is divided into several modules, making it easier to maintain and scale.
*   **Built with modern technologies:**
    *   [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) for sharing code between platforms.
    *   [Jetpack Compose](https://developer.android.com/jetpack/compose) for building the UI.

## Modules

*   `app`: The main application module that brings everything together.
*   `engine`: The core game logic.
*   `ui-lobby`: The lobby screen where players can start or join a game.
*   `presentation-*`: Modules related to the presentation layer.
*   `libs-*`: Utility modules.

## Building and Running

### Prerequisites

*   Java Development Kit (JDK) 11 or higher.
*   Android Studio (for Android development).

### Building

To build the project, run the following command in the root directory:

```bash
./gradlew build
```

### Running

#### Desktop

To run the desktop version, use the following command:

```bash
./gradlew :desktop:run
```

#### Android

To run the Android version, open the project in Android Studio and run the `android` configuration.

## Contributing

Contributions are welcome! If you want to contribute to this project, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes.
4.  Submit a pull request.
