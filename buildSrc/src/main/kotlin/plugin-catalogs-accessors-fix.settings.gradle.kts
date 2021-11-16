import org.gradle.api.internal.FeaturePreviews
import org.gradle.api.internal.FeaturePreviews.Feature.VERSION_CATALOGS
import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

if (serviceOf<FeaturePreviews>().isFeatureEnabled(VERSION_CATALOGS))
  gradle.rootProject {
    val accessors = files(serviceOf<DependenciesAccessors>().classes.asFiles)
    buildscript { dependencies.classpath(accessors) }
  }
