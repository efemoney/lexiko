@file:Suppress(
  "UnstableApiUsage",
  "DEPRECATION",
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE",
)

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.extension.AndroidComponentsExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

inline fun PluginManager.withAnyPlugin(vararg plugins: String, action: Action<AppliedPlugin>) =
  plugins.forEach { withPlugin(it, action) }

inline fun PluginManager.withAnyKotlinPlugin(action: Action<AppliedKotlinPlugin<KotlinProjectExtension>>) {
  withAnyPlugin(
    "org.jetbrains.kotlin.js",
    "org.jetbrains.kotlin.jvm",
    "org.jetbrains.kotlin.android",
    "org.jetbrains.kotlin.multiplatform",
  ) {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun PluginManager.withMultiplatformPlugin(action: Action<AppliedKotlinPlugin<KotlinMultiplatformExtension>>) {
  withAnyPlugin("org.jetbrains.kotlin.multiplatform") {
    action.execute(AppliedKotlinPlugin(this))
  }
}

inline fun PluginManager.withAnyAndroidPlugin(action: Action<AppliedAndroidPlugin>) {
  withAnyPlugin(
    "com.android.application",
    "com.android.library",
  ) {
    action.execute(AppliedAndroidPlugin(this))
  }
}


class AppliedAndroidPlugin(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {

  inline val Project.android
    get() = extensions.getByName<CommonExtension<*, *, *, *>>("android")

  inline fun Project.android(action: Action<CommonExtension<*, *, *, *>>) =
    extensions.configure("android", action)

  inline val Project.androidComponents
    get() = extensions.getByName<AndroidComponentsExtension<*, *, *>>("androidComponents")

  inline fun Project.androidComponents(action: Action<AndroidComponentsExtension<*, *, *>>) =
    extensions.configure("androidComponents", action)
}

class AppliedKotlinPlugin<T : KotlinProjectExtension>(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {
  inline val Project.kotlin
    get() = extensions.getByName<KotlinProjectExtension>("kotlin") as T

  inline fun Project.kotlin(action: Action<T>) =
    extensions.configure("kotlin", action)
}
