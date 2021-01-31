# zuper-test

### Overview

As soon as the app is opened, it downloads the JavaScript code from the AWS bucket and then starts 200 operations, displaying them in a `RecyclerView`.

The app uses a `WebView` to execute the JavaScript code, injecting a Kotlin object to the JavaScript environment using the `WebView.addJavascriptInterface` method. This object has the expected `postMessage` method required by the operations JavaScript code to start and update the operations (through the `window.jumbo` reference).

### Architecture
Currently, the app has a `Clean Architecture`-like architecture.

To handle the user interaction and displaying data, it has an UI layer (`app` module) following the Model-View-ViewModel (MVVM) pattern.

To define the bussiness entities and mechanisms to operate with them (repositories and use cases), it has a domain layer (`domain-layer` module). Usually, it would have a package for the use cases, but given the nature of the current app, I decided to access the repositories directly in the `ViewModel` (instead of creating use cases wrapping each repository method / action). 

Finally, it has a data layer (`data-layer` module) that encapsulates the process of providing data for the app using different sources like the network, and in this case, a locally running JavaScript interpreter (`WebView`).

### Dependencies
- **Kotlin Coroutines** to handle asynchronous work and reactive streams.
- **Retrofit** and **Moshi** for HTTP requests and parsing JSON objects.
- **Dagger Hilt** for dependency injection.
- **Jetpack Lifecycle** for lifecycle aware components (`ViewModel` and `LiveData`).
- **JUnit** and **Hamcrest** for unit testing.

### Tests
Only the `OperationRepositoryImpl` class and the `MessageModel.toDomain()` method in the `data-layer` module were unit tested as they have most of the app's logic (besides the JavaScript layer).

We could also easily unit test the `OperationsViewModel` using `robolectric`. 

For example, we could test that the `OperationsViewModel.showErrorState` stream emits `true` when the repository intialization fails and `false` when it succeeds (injecting a fake `OperationRepository` to arrange these cases).
