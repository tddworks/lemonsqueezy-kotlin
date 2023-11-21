This is a Kotlin Multiplatform project targeting iOS.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…


## Lemonsqueezy License api

```kotlin
val api = LemonSqueezyLicenseApi(
            HttpRequester.default(
                createHttpClient(
                    url = "api.lemonsqueezy.com",
                    token = "YOUR_TOKEN",
                    engine = CIO
                )
            )
        )

val r = api.activeLicense("YOUR_LICENSE_KEY", "YOUR_INSTANCE_NAME")
```


