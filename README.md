# MusicApp-Kotlin
Created a music application using Kotlin where it will create a visualizer according to the pitch of the voice of the sound/music

## Prerequisites

Before you begin, ensure you have the following installed:

- Kotlin Compiler: [Download Kotlin](https://kotlinlang.org/docs/getting-started.html)
- Java Development Kit (JDK): [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

## Android App Permissions

This Android app requires the following permissions to function properly:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```
## Library Used for audio visualizer
We used the [Android Audio Visualizer](https://github.com/GautamChibde/android-audio-visualizer/wiki/Line-Visualizer) Library to enhance the visual experience. You can include it in your project by adding the following dependency:

```xml
dependencies {
//...
implementation("io.github.gautamchibde:audiovisualizer:2.2.5")
...//
}
```
### Usage
1) Launch the application.

2) Press the "Play" button to start the music.

3) Observe the visualizer reacting to the audio.

### Video preview
Watch a demo video of the app here.

https://github.com/IPH-Technologies-Pvt-Ltd/Music-Visualizer-Kotlin/assets/133771823/eb205a69-3ab7-4673-8b4e-648b9b24d349



