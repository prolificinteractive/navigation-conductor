import Versions.archNavigationVersion
import Versions.archPagingVersion
import Versions.archRoomVersion
import Versions.archWorkVersion
import Versions.glideVersion
import Versions.junitVersion
import Versions.kotlinVersion
import Versions.lifecycleVersion

@Suppress("MayBeConstant", "MemberVisibilityCanBePrivate", "unused")
object Deps {

  // Support Libraries
  val appcompatV7 = "androidx.appcompat:appcompat:1.0.0-alpha1"
  val design = "com.google.android.material:material:1.0.0-alpha1"
  val recyclerviewV7 = "androidx.recyclerview:recyclerview:1.0.0-alpha1"
  val supportAnnotations = "androidx.annotation:annotation:1.0.0-alpha1"
  val cardView = "androidx.cardview:cardview:1.0.0-alpha1"
  val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.0"
  val multidex = "androidx.multidex:multidex:2.0.0"

  // Play Services
  // https://developers.google.com/android/guides/setup
  val playAuth = "com.google.android.gms:play-services-auth:15.0.0"

  // Firebase
  // https://firebase.google.com/support/release-notes/android
  val firebaseConfig = "com.google.firebase:firebase-config:15.0.2"
  val firebaseCore = "com.google.firebase:firebase-core:15.0.2"
  val firebasePerf = "com.google.firebase:firebase-perf:15.2.0"
  val firebaseMessaging = "com.google.firebase:firebase-messaging:15.0.2"

  /**
   * Architecture Components
   */

  // ViewModel and LiveData
  val archEx = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
  val archJava = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
  val archRuntime = "androidx.lifecycle:lifecycle-runtime:$lifecycleVersion"
  val archViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
  val lifecycleCompilers = "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
  val archReactiveStreams = "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion"
  // optional - Test helpers for LiveData
  val archCoreTesting = "androidx.arch.core:core-testing:$lifecycleVersion"

  // Navigation
  val archNavigationFragment = "android.arch.navigation:navigation-fragment-ktx:$archNavigationVersion"
  val archNavigation = "android.arch.navigation:navigation-ui-ktx:$archNavigationVersion"

  // Room
  val archRoomRuntime = "android.arch.persistence.room:runtime:$archRoomVersion"
  val archRoomProcessor = "android.arch.persistence.room:compiler:$archRoomVersion"
  val archRoomRxJava = "android.arch.persistence.room:rxjava2:$archRoomVersion"
  val archRoomTesting = "android.arch.persistence.room:testing:$archRoomVersion"

  // Paging
  val archPagingRuntime = "android.arch.paging:runtime:$archPagingVersion"
  // optional - RxJava support, currently in alpha
  val archPagingRxJava = "android.arch.paging:rxjava2:1.0.0-alpha1"
  // alternatively - without Android dependencies for testing
  val archPagingTesting = "android.arch.paging:common:$archPagingVersion"

  // Worker
  val archWorkRuntime = "android.arch.work:work-runtime-ktx:$archWorkVersion"
  // optional - Firebase JobDispatcher support
  val archWorkFirebase = "android.arch.work:work-firebase:$archWorkVersion"
  // optional - Test helpers
  val archWorkTesting = "android.arch.work:work-testing:$archWorkVersion"

  val arch = listOf(
      archEx,
      archJava,
      archReactiveStreams,
      archNavigationFragment,
      archNavigation
//      archRoomRuntime,
//      archPagingRuntime,
//      archPagingRxJava,
//      archWorkRuntime,
//      archWorkFirebase
  )

  val archAP = listOf(
      archRoomProcessor
  )

  val archTesting = listOf(
      archCoreTesting,
      archRoomTesting,
      archPagingTesting,
      archWorkTesting
  )


  // Kotlin
  val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"

  // Glide
  val glide = "com.github.bumptech.glide:glide:$glideVersion"
  val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"

  val timber = "com.jakewharton.timber:timber:4.6.1"
  val conductor = "com.bluelinelabs:conductor:2.1.4"

  // Testing
  val jUnit = "junit:junit:$junitVersion"
  val mockito = "org.mockito:mockito-core:2.18.3"
  val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1"
  val hamcrest = "org.hamcrest:hamcrest-all:1.3"
  val kluent = "org.amshove.kluent:kluent:1.38"
  val kluentAndroid = "org.amshove.kluent:kluent-android:1.38"
  val robolectric = "org.robolectric:robolectric:4.0-alpha-3"
  val robolectricMultiDex = "org.robolectric:shadows-multidex:4.0-alpha-3"

  val testRunner = "com.android.support.test:runner:1.0.2"
  val testRules = "com.android.support.test:rules:1.0.2"
  val espressoCore = "com.android.support.test.espresso:espresso-core:3.0.2"

  // Dependency Group
  val supportLibs = listOf(
      appcompatV7,
      cardView,
      constraintLayout,
      design,
      multidex,
      recyclerviewV7,
      supportAnnotations
  )

  val firebase = listOf(
      firebaseConfig,
      firebaseCore,
      firebasePerf,
      firebaseMessaging)

  val testLibs = listOf(jUnit, robolectric, robolectricMultiDex, mockito, hamcrest, kluent, kluentAndroid)

  val androidTestLibs = listOf(testRules, testRunner, espressoCore)
}

