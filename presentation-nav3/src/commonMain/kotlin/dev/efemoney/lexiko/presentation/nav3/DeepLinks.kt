package dev.efemoney.lexiko.presentation.nav3

import androidx.compose.ui.util.fastFold
import com.eygraber.uri.Uri
import dev.efemoney.lexiko.presentation.Screen
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer

inline fun <reified T : Screen> resolveUri(base: Uri, screen: T): Uri {
  return Json.encodeToJsonElement(serializer<T>(), screen).jsonObject
    .ifEmpty { return base }
    .let { resolveUri(base, it) }
}

@PublishedApi
internal fun resolveUri(base: Uri, variables: JsonObject): Uri {
  return base
    .buildUpon()
    .path(base.path?.replace(VariableRegex) { it.value.maybeReplace(variables) })
    .query(base.query?.replace(VariableRegex) { it.value.maybeReplace(variables) })
    .build()
}

private fun String.maybeReplace(variables: JsonObject): String {
  if (!startsWith('{') || !endsWith('}')) return this
  val path = substring(1, length - 1).split('.').ifEmpty { return this }

  val element = path.fastFold(variables as JsonElement) { acc, key ->
    when (acc) {
      is JsonObject -> acc[key] ?: JsonNull
      is JsonArray -> acc[key.toInt()]
      else -> JsonNull
    }
  }
  return element.jsonPrimitive.content
}

private val VariableRegex = Regex("\\{[\\w._]+}")
