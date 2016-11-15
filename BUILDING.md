# Building the 10Duke Android Client Libraries

## Requirements

* JDK installed and in path
* Maven installed and in path
* 10Duke client libraries available in e.g. local Maven repository (see below)


### Install 10Duke client libraries

This project depends on [Java-client core libraries (java-client-core)](https://github.com/10Duke/java-client-core).
Please follow the instructions on that repository.


### Preparing the Android SDK

If you already have Android SDK and necessary dependencies, make sure environment `ANDROID_HOME` is
set properly.

Following shell-variables are needed by configuration scripts below (they are not needed once the
configuration is finished):

```sh
# These versions are necessary for working build:
SDK_VERSION=24.4.1
SUPPORT_VERSION=24.2.1
API_LEVEL=24

# Some following scripts may use SRC, which should point to the project root
# Change the path below to point to the project root
SRC=${HOME}/git/github/10duke/android-client-idp-sso
```


## Downloading and Setting Up The Android SDK

You can skip this part, if you have Android SDK and the necessary components. **NOTE**: Even if
you have Android SDK, you may need to install additional Android SDK-components as per "Install
needed dependencies" in following script.

Following scripts install the necessary Android components in Linux environment.

(The scripts assume you have `curl` installed)

```sh
# Android SDK is downloaded to ANDROID_BASE
ANDROID_BASE=${HOME}
export ANDROID_HOME=${ANDROID_BASE}/android-sdk-linux

# Download, and install the initial Android SDK
mkdir -p ${ANDROID_BASE}
cd ${ANDROID_BASE}

curl -O https://dl.google.com/android/android-sdk_r${SDK_VERSION}-linux.tgz
sha1sum android-sdk_r${SDK_VERSION}-linux.tgz

tar -xzf android-sdk_r${SDK_VERSION}-linux.tgz


# Update and download the missing Android SDK pieces:
cd ${ANDROID_HOME}/tools

# List available packages (no need to execute)
#./android list sdk  --extended --all

# Install needed dependencies
# NOTE: These ask to accept license
./android update sdk --filter android-16,android-24,build-tools-24.0.1,extra-android-m2repository,platform-tools,sys-img-armeabi-v7a-android-16 --all --no-ui
```


## Installing the Android SDK to local Maven repository:

The project uses Android support annotations (`@NonNull`, `@Nullable`), and expects to find the
support-annotations-${SUPPORT_VERSION} from Maven. Also, parts of the project depend directly on
Android SDK 24. Unfortunately, neither artifacts are available in public repositories, so you need
to install them locally:

```sh
# Support annotations
cd ${ANDROID_HOME}/extras/android/m2repository/com/android/support/support-annotations/${SUPPORT_VERSION}
mvn install:install-file \
  -DpomFile=support-annotations-${SUPPORT_VERSION}.pom \
  -Dfile=support-annotations-${SUPPORT_VERSION}.jar

# The SDK itself
cd ${ANDROID_HOME}/platforms/android-${API_LEVEL}
mvn install:install-file \
  -Dfile=android.jar \
  -DgroupId=com.android \
  -DartifactId=sdk \
  -Dversion=${API_LEVEL} \
  -Dpackaging=jar
```


## Building

```sh
# cd to the project directory:
cd ${SRC}

# Build the maven-artifacts
mvn clean install

# Build the SSO .aar and install it to the local Maven repository
cd com.tenduke.client.android.sso
./gradlew assembleDebug install

# Build the test
cd ../com.tenduke.client.android.tests
./gradlew assembleDebug
```

This will be changed in future to work from single Gradle-script.


## Running tests

```sh
cd ${ANDROID_HOME}/tools

# Create virtual device
./android create avd -n tst-android-16-arm -t android-16

# Start the emulator
./emulator64-arm -avd tst-android-16-arm

# Run the instrumented tests
cd ${SRC}/com.tenduke.client.android.tests
./gradlew connectedAndroidTest
```


## OS X troubleshooting

If you get the following error when running Android emulator from command line:

```sh
dyld: Library not loaded: libQt5Widgets.5.dylib
  Referenced from: /usr/local/Cellar/android-sdk/24.4.1_1/tools/./emulator64-arm
  Reason: image not found
Abort trap: 6

# Add an export to the DYLD_LIBRARY_PATH as below
export DYLD_LIBRARY_PATH="$ANDROID_HOME/tools/lib64:$ANDROID_HOME/tools/lib64/qt/lib:$DYLD_LIBRARY_PATH"
```
