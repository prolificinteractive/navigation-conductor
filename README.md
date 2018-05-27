# Navigation Component for Conductor




## Installation

Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.prolificinteractive.navigation:navigation-conductor:0.1.0'
}
```

### Usage


```xml
<?xml version="1.0" encoding="utf-8"?>
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:fitsSystemWindows="true">
  
      <com.prolificinteractive.conductor.archnavigation.NavHostLayout
          android:id="@+id/controller_container"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          app:defaultNavHost="true"
          app:navGraph="@navigation/nav_graph_controller" />
  
  </FrameLayout>
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    app:startDestination="@+id/firstController">

    <controller
        android:id="@+id/firstController"
        android:name="com.prolificinteractive.conductornav.DemoController"
        android:label="DemoController"
        tools:layout="@layout/controller_navigation_demo">

        <argument
            android:name="index"
            android:defaultValue="0"
            app:type="integer" />
        <argument
            android:name="displayUpMode"
            android:defaultValue="0"
            app:type="integer" />
    </controller>
</navigation>
```

## Contributing to navigation-conductor

To report a bug or enhancement request, feel free to file an issue under the respective heading.

If you wish to contribute to the project, fork this repo and submit a pull request. Code contributions should follow the standards specified in the [Prolific Android Style Guide](https://github.com/prolificinteractive/android-code-styles).

## License

![prolific](https://s3.amazonaws.com/prolificsitestaging/logos/Prolific_Logo_Full_Color.png)

geocoder is Copyright (c) 2018 Prolific Interactive. It may be redistributed under the terms specified in the [LICENSE] file.

[LICENSE]: ./LICENSE
