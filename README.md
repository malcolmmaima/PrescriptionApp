
## Prescription App (Compose)

This is a modular Android application designed with clean architecture principles. It provides a user-friendly interface for viewing medical data, allowing users to browse and view their prescription items.

### Screenshots

##### Light
<a href="url"><img src=https://github.com/user-attachments/assets/896fbbfe-7d6b-4ae6-9c54-cd36a15800d3 height="450"></a>
<a href="url"><img src=https://github.com/user-attachments/assets/337add99-6963-4407-b88f-88715d5df050 height="450"></a>
<a href="url"><img src=https://github.com/user-attachments/assets/2e55e0d3-b2bf-4a47-8ee2-a849d40e693a height="450"></a>
<a href="url"><img src=https://github.com/user-attachments/assets/0e180e7b-d0a0-4b9e-b45a-c740dbd08ece height="450"></a>

##### Dark
<a href="url"><img src=https://github.com/user-attachments/assets/fc2dc345-b2e8-4a7b-9a5c-47b6a11fc16b height="450"></a>
<a href="url"><img src=https://github.com/user-attachments/assets/c4f3367c-9cfb-4618-bce9-1dd2f535ce6d height="450"></a>
<a href="url"><img src=https://github.com/user-attachments/assets/b0d14d39-9cc2-4631-a9ed-8b2c0eec9c99 height="450"></a>


### Architecture
This follows a modular architecture adhering to clean architecture principles and the MVVM (Model-View-ViewModel) pattern.

### Architecture Overview

<img width="450" alt="image" src="https://github.com/user-attachments/assets/c44252cf-da55-44a7-8517-c0e90a556482">


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
The app APK can be found from the latest successful action on the GitHub Actions tab, Scroll down to artifacts <a href="https://github.com/malcolmmaima/PrescriptionApp/actions">Actions</a>
