# Android Support for 10Duke Client Libraries

This repository provides Android-library for Single-Sign-On -functionality.

For working example, please see project [android-client-sample](https://github.com/10Duke/android-client-sample).

Looking for the REST-API client-libraries? The Java-client libraries are designed to work with Android:
* [Java-client core libraries](https://github.com/10Duke/java-client-core)
* [Java-client for 10Duke Identity Provider REST API](https://github.com/10Duke/java-client-idp)


## Building the project

Please see [BUILDING.md](BUILDING.md)


## Dependencies

Android client depends on 10Duke Java client libraries and needs same runtime dependencies. Please
see the [10Duke Java-client core libraries](https://github.com/10Duke/java-client-core).

For example, using Gson as JSON-serializer, the dependencies could be:

```gradle
// IDP API:
compile 'com.tenduke.client.api:idp:1.0-SNAPSHOT'

// SSO API from this repo:
compile(group: 'com.tenduke.client.android', name: 'sso', version: '1.0', ext: 'aar'){
    transitive=true
}

// 10Duke REST-API helpers:
// (Assuming Gson)
compile 'com.tenduke.client:gson:1.0-SNAPSHOT'
compile 'com.tenduke.client:utils:1.0-SNAPSHOT'

// Retrofit-dependencies
// (Assuming Gson)
compile 'com.squareup.okhttp3:okhttp:3.4.1'
compile 'com.squareup.retrofit2:retrofit:2.1.0'
compile 'com.squareup.retrofit2:converter-gson:2.1.0'
compile 'com.google.code.gson:gson:2.7'
```

## Example usage

### Example login

```java
public class MyActivity extends Activity {

    protected static final int REQUEST_CODE_LOGIN = 2;

    /** Starts the Login-activity.
     *
     */
    public void login () {
        startActivityForResult (
                LoginActivity.createIntent(
                        this,
                        getString(R.string.action_login),   // Title on the login action bar
                        "https://vslidp.10duke.com",        // URL of the SSO-service. Replace with correct value
                        "android_sample",                   // Client ID. Replace with correct value.
                        null                                // No nonce in this example
                ),
                REQUEST_CODE_LOGIN
        );
    }

    /** Handle the login-result:
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //
        super.onActivityResult(requestCode, resultCode, data);
        //
        switch (requestCode) {

            case REQUEST_CODE_LOGIN:
                // Handle the login results here
                break;
        }
    }
}
```

### Example logout

```java
public class MyActivity extends Activity {

    protected static final int REQUEST_CODE_LOGOUT = 1;

    /** Starts the Logout-activity.
     *
     */
    protected void logout (@NonNull final ApiCredentials credentials) {
        //
        startActivityForResult (
                LogoutActivity.createIntent(
                        this,
                        getString(R.string.action_logout),          // Title on the logout action bar
                        "https://vslidp.10duke.com",                // URL of the SSO-service. Replace with correct value
                        "https://localhost/oauth2_callback.html",   // Callback URL. NOTE: This must be configured in the
                                                                    // IdentityProvider.
                        credentials),                               // Credentials to log out
                REQUEST_CODE_LOGOUT
        );
    }

    /** Handle the logout-result:
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //
        super.onActivityResult(requestCode, resultCode, data);
        //
        switch (requestCode) {

            case REQUEST_CODE_LOGOUT:
                // Handle the logout results here
                break;
        }
    }
}
```


## Artifacts

The project provides several small artifacts. The following artifacts are provided:

* `com.tenduke.client.android.android-utils`: Android specific utilities, for manipulating e.g.
  `Bundles` etc.
* `com.tenduke.client.android.api`: Common utilities for registering APIs in Android.
* `com.tenduke.client.android.security`: Security-related utilities. Currently mostly for persisting
  `ApiCredential`s.
* `com.tenduke.client.android.storage`: Storage-related utilities. Currently mostly for persisting
  `ApiCredential`s.
* `com.tenduke.client.android.sso`: Single-Sign-On API. This is an .aar-archive: See `build.gradle`
  for an example how to declare dependency to this archive.
* `com.tenduke.client.android.tests`: For running instrumented tests

Of these only `com.tenduke.client.android.sso` is likely to be required. The other artifacts are not
necessary, if you don't need the functionality or you plan to implement the functionality yourself.

## Using Login / Logout

Project `com.tenduke.client.android.sso` contains login/logout for Android. Login uses OAuth2
implicit flow via Android's embedded browser.

Login and logout are implemented as Android Fragments, but simple basic Activities for login/logout
are also supplied. The fragments implement the actual functionality and the login/logout Activities
merely host the fragments. The classes are:

* `com.tenduke.client.android.sso.oauth2.OAuth2LoginFragment`
* `com.tenduke.client.android.sso.oauth2.OAuth2LogoutFragment`
* `com.tenduke.client.android.sso.LoginActivity`
* `com.tenduke.client.android.sso.LogoutActivity`

The classes have javadocs, which describe parameters and return values, and provide sample usage.








