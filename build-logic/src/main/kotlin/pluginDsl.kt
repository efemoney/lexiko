@file:Suppress("NOTHING_TO_INLINE")

import org.gradle.plugin.use.PluginDependenciesSpec

inline fun PluginDependenciesSpec.plugin(suffix: String) = id("plugin-$suffix")
