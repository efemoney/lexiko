package dev.efemoney.lexiko

import com.serjltt.moshi.adapters.FirstElement
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import dagger.Reusable
import java.io.IOException
import java.lang.reflect.Type

/**
 * Added to allow serialization of Concrete collection types (ArrayList, HashSet etc) as Moshi doesn't support
 * serializing those types out of the box (only the base classes List, Set etc)
 *
 * Needed because of firebase admin for some mundane reason I do not remember
 */
private abstract class MutableCollectionJsonAdapter<C : MutableCollection<T>, T> private constructor(
  private val elementAdapter: JsonAdapter<T>
) : JsonAdapter<C>() {

  @Throws(IOException::class)
  override fun fromJson(reader: JsonReader) = newCollection().apply {
    reader.beginArray()
    while (reader.hasNext()) add(elementAdapter.fromJson(reader)!!)
    reader.endArray()
  }

  @Throws(IOException::class)
  override fun toJson(writer: JsonWriter, value: C?) {
    writer.beginArray()
    for (element in value!!) elementAdapter.toJson(writer, element)
    writer.endArray()
  }

  override fun toString() = "$elementAdapter.collection()"

  abstract fun newCollection(): C

  companion object FACTORY : Factory {

    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
      val rawType = Types.getRawType(type)

      return when {
        annotations.isNotEmpty() -> null
        rawType == ArrayList::class.java -> newArrayListAdapter<Any>(type, moshi).nullSafe()
        else -> null
      }
    }

    private fun <T> newArrayListAdapter(type: Type, moshi: Moshi): JsonAdapter<MutableCollection<T>> {
      val elementType = Types.collectionElementType(type, MutableCollection::class.java)
      val elementAdapter: JsonAdapter<T> = moshi.adapter(elementType)
      return object : MutableCollectionJsonAdapter<MutableCollection<T>, T>(elementAdapter) {
        override fun newCollection(): MutableCollection<T> = ArrayList()
      }
    }
  }
}

@Module
interface MoshiModule {

  companion object {

    @Provides
    @Reusable
    fun moshi(): Moshi = Moshi.Builder()
      .add(Wrapped.ADAPTER_FACTORY)
      .add(FirstElement.ADAPTER_FACTORY)
      .add(MutableCollectionJsonAdapter.FACTORY)
      .build()
  }
}
