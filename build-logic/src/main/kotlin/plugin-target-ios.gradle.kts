pluginManager.withMultiplatformPlugin {
  val projectName = project.name

  kotlin.ios {
    binaries.framework {
      baseName = projectName
    }
  }
}
