plugins {
  com.android.application
}

android.buildTypes.configureEach {
  setMatchingFallbacks("release")
}
