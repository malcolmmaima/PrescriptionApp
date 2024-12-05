
# Prescription App (MVVM with Jetpack Compose)

This is a simple Kotlin-based Android application following the MVVM architecture pattern. The app uses Jetpack Compose for UI, Hilt for dependency injection, Room for local storage, and a mock API for retrieving medical data. The app is designed to demonstrate various Android development skills, including UI design, state management, database integration, and testing.

## Features

1. **Login Screen**: A simple login screen where users can input their username or email.

2. **Personalized Greeting**: After logging in, the app greets the user based on the time of day (Morning, Afternoon, or Evening) and displays the username/email entered in the login screen.

3. **Medicine List**: The app displays a list of medicines, fetched from a mock API (using Mocky.io), showing the name, dose, and strength of each medicine. Each medicine is displayed in a card. Tapping on any card opens a detailed view with the same data.

4. **Room Database**: The app uses Room DB to store any relevant data, such as the logged-in user's details and medicine data for offline access.

5. **Unit Testing**: Unit tests for the viewmodels and repositories are written to validate use cases in the app.

### Screenshots

##### Light
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132821.png height="450"></a>
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132543.png height="450"></a>
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132625.png height="450"></a>
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132719.png height="450"></a>

##### Dark
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132932.png height="450"></a>
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132840.png height="450"></a>
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132859.png height="450"></a>
<a href="url"><img src=https://github.com/malcolmmaima/PrescriptionApp/blob/main/screenshots/Screenshot_20240803_132912.png height="450"></a>

### Architecture
This follows a modular architecture adhering to clean architecture principles and the MVVM (Model-View-ViewModel) pattern.

### Architecture Overview

<img width="901" alt="image" src="https://github.com/user-attachments/assets/7946fb17-cf16-4219-8c70-bfd1ba08653d">


### Modules
#### App Module:

- Depends on all relevant feature modules (:features:prescription).

#### Feature Modules:

- Represents individual features of the app (:features:prescription, :feature:anotherfeature).
- It should not depend on other feature modules.
- Depends on core modules for shared functionality.

#### Core Modules:

- Includes common code shared among features and other core modules (e.g., :core:design, :core:networking, :core:utils, :core:database).
  Can depend on one another but should avoid cyclical dependencies.

## How to Build and Run

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/yourusername/prescription-app.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

### Unit Tests

- Three unit tests are written to ensure the functionality of core use cases.
- Tests can be found in the `test` directory within the app module.

### Build Process

1. **Install dependencies**:
  - Ensure you have the required SDK versions and dependencies.

2. **Run Unit Tests**:
  - Execute tests using the following Gradle command:
   ```bash
   ./gradlew testDebugUnitTest
   ```

3. **Build APK**:
  - To build the APK:
   ```bash
   ./gradlew :app:assembleDebug
   ```

#### Libraries and Tools
- Github Actions: Automates CI/CD workflows and manages pipelines for the project.
- Kotlin (1.9.0): The primary language for app development and testing.
- Coil (2.4.0): Handles image loading efficiently within Compose UI components.
- Hilt (2.48): Manages dependency injection with ease and efficiency.
- JUnit (4.13.2): Framework used for writing and running unit tests.
- Jetpack Compose (2023.09.00): Used for creating modern, reactive UIs and navigation.
- Accompanist (0.31.5-beta): Provides additional Compose functionalities not yet available in the core library.
- Material Icons (1.9.0): Supplies various icons that enhance the visual design of the app.
- Turbine (0.12.1): Specialized library for testing kotlinx.coroutines Flow.
- Kotlin Coroutines (1.7.3): Facilitates asynchronous programming and managing background tasks.
- Room (2.5.2): Provides an abstraction layer over SQLite for robust and efficient database operations.
- Retrofit (2.9.0): Simplifies REST API interactions and data fetching.
- OkHttp (4.11.0): Manages network operations and logging for HTTP requests.
- Build Logic Convention: Defines and maintains consistent build practices and management.
- Moshi (1.15.0): Converts JSON into Kotlin objects and vice versa.

#### App Artifacts
The app APK can be found from the latest successful action on the GitHub Actions tab <a href="https://github.com/malcolmmaima/PrescriptionApp/actions">Actions</a>
