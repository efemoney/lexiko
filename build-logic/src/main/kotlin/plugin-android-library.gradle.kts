plugins {
  id("convention-android")
  com.android.library
}

androidComponents {
  // beforeVariants(
  //   selector = selector().withBuildType("release"),
  //   callback = { it.enabled = false }
  // )
}