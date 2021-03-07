package dev.ebnbin.openmojipicker

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.ebnbin.eb.app
import java.util.Locale

object OpenMojiCache {
    internal val openMojiList: List<OpenMoji> by lazy {
        GsonBuilder().create().fromJson(
            app.resources.openRawResource(R.raw.openmoji).bufferedReader(),
            object : TypeToken<List<OpenMoji>>() {}.type,
        )
    }

    internal val openMojiMap: Map<String, OpenMoji> by lazy {
        openMojiList
            .map { it.hexcode to it }
            .toMap(linkedMapOf())
    }

    private val drawableIdMap: MutableMap<String, Int> = mutableMapOf()

    fun getDrawableId(hexcode: String): Int {
        return drawableIdMap.getOrPut(hexcode) {
            kotlin.runCatching {
                val name = "openmoji_${hexcode.toLowerCase(Locale.ROOT).replace("-", "_")}"
                R.drawable::class.java.getField(name).getInt(null)
            }.getOrDefault(0)
        }
    }

    private val drawable48IdMap: MutableMap<String, Int> = mutableMapOf()

    fun getDrawable48Id(hexcode: String): Int {
        return drawable48IdMap.getOrPut(hexcode) {
            kotlin.runCatching {
                val name = "openmoji_48_${hexcode.toLowerCase(Locale.ROOT).replace("-", "_")}"
                R.drawable::class.java.getField(name).getInt(null)
            }.getOrDefault(0)
        }
    }
}
