# Navigation Component for Conductor

The Android Architecture Navigation Component’s documentation has a section that suggests it is possible to add “new destination types” outside of Activities and Fragments. 

###### [Add support for new destination types](https://developer.android.com/topic/libraries/architecture/navigation/navigation-add-new) 

> [NavControllers](https://developer.android.com/reference/androidx/navigation/NavController.html) rely on one or more [Navigator](https://developer.android.com/reference/androidx/navigation/Navigator.html) objects to perform the navigation operation. To be able to navigate to any other type of destination, one or more additional [Navigator](https://developer.android.com/reference/androidx/navigation/Navigator.html) objects must be added to the [NavController](https://developer.android.com/reference/androidx/navigation/NavController.html). For example, when using fragments as destinations, the [NavHostFragment](https://developer.android.com/reference/androidx/navigation/fragment/NavHostFragment.html) automatically adds the [FragmentNavigator](https://developer.android.com/reference/androidx/navigation/fragment/FragmentNavigator.html) class to its [NavController](https://developer.android.com/reference/androidx/navigation/NavController.html). 

The same was suggested on [episode 92](http://androidbackstage.blogspot.com/2018/05/episode-92-navigation-controller.html) of the Android Developers Backstage podcast and at [Google IO 2018](https://www.youtube.com/watch?v=8GCXtCjtg40). There is no shortage of libraries or frameworks utilizing Views as the basis for navigation. Assuming the implementation of the Navigation Architecture Component is generic enough, could it be possible to integrate one of these View-based frameworks with Navigation Component? So far, one has stood above the rest (if Github stars are any indication of success), [Conductor](https://github.com/bluelinelabs/Conductor).

This library is an attempt to implement a Conductor integration for the Navigation Architecture Component. More details about the implementation are in [Navigating Conductor and the Navigation Architecture Component](https://medium.com/prolific-interactive/navigating-conductor-and-the-navigation-architecture-component-4145b5ad9bcf). If this is your first introduction to the Navigation Architecture Component, I would recommend reading the excellent [A problem like Navigation](https://medium.com/a-problem-like-maria/a-problem-like-navigation-e9821625a70e) series by [Maria Neumayer](https://medium.com/@marianeum) or [Android Navigation Components](https://medium.com/google-developer-experts/android-navigation-components-part-1-236b2a479d44) by [Dario Mungoi](https://medium.com/@mungoidario?source=post_header_lockup)


### Usage


```xml
res/layout/activity.xml

<FrameLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
  
    <com.prolificinteractive.conductor.archnavigation.NavHostLayout
        android:id="@+id/controller_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:navGraph="@navigation/graph"/>

</FrameLayout>
```

```xml
res/navigation/graph.xml

<navigation 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
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

## Contributing to navigation-conductor

To report a bug or enhancement request, feel free to file an issue under the respective heading.

If you wish to contribute to the project, fork this repo and submit a pull request. Code contributions should follow the standards specified in the [Prolific Android Style Guide](https://github.com/prolificinteractive/android-code-styles).

## License

![prolific](https://s3.amazonaws.com/prolificsitestaging/logos/Prolific_Logo_Full_Color.png)

geocoder is Copyright (c) 2018 Prolific Interactive. It may be redistributed under the terms specified in the [LICENSE] file.

[LICENSE]: ./LICENSE
