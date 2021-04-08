@file:Suppress(
  "UnstableApiUsage",
  "DEPRECATION",
  "NOTHING_TO_INLINE",
  "TYPEALIAS_EXPANSION_DEPRECATION",
)

import com.android.build.gradle.BaseExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

inline fun PluginManager.withAnyPlugin(vararg plugins: String, action: Action<AppliedPlugin>) =
  plugins.forEach { withPlugin(it, action) }

inline fun PluginManager.withAnyAndroidPlugin(action: Action<AppliedAndroidPlugin>) {
  withAnyPlugin("android", "android-library") {
    action.execute(AppliedAndroidPlugin(this))
  }
}

class AppliedAndroidPlugin(appliedPlugin: AppliedPlugin) : AppliedPlugin by appliedPlugin {

  val Project.android get() = the<BaseExtension>()

  fun Project.android(action: Action<BaseExtension>) = configure(action::execute)
}
