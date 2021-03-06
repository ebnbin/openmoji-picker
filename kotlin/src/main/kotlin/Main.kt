import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

fun main() {
//    json()
//    filter()
//    writeJsonFiltered()
}

private val gson: Gson by lazy {
    GsonBuilder().create()
}

private val gsonPrettyPrinting: Gson by lazy {
    GsonBuilder().setPrettyPrinting().create()
}

private val openMojiList: List<OpenMoji> by lazy {
    val json = File("file", "openmoji.json").reader()
    val typeOfT = object : TypeToken<List<OpenMoji>>() {}.type
    gson.fromJson(json, typeOfT)
}

private fun json() {
    println(openMojiList)
}

private fun filter(): List<OpenMoji> {
    return openMojiList
        .also {
            println(it.size)
        }
        .let { list ->
            list.filterNot {
                it.group == "extras-openmoji" // 317
            }.also {
                println("extras-openmoji: ${list.size - it.size}, ${it.size}")
            }
        }
        .let { list ->
            list.filterNot {
                it.group == "extras-unicode" // 57
            }.also {
                println("extras-unicode: ${list.size - it.size}, ${it.size}")
            }
        }
        .let { list ->
            list.filterNot {
                it.skintone.isNotEmpty() // 皮肤 1490
            }.also {
                println("skintone: ${list.size - it.size}, ${it.size}")
            }
        }
        .let { list ->
            list.filterNot {
                it.group == "component" && it.subgroups == "skin-tone" // 皮肤 5
            }.also {
                println("skin-tone: ${list.size - it.size}, ${it.size}")
            }
        }
        .let { list ->
            list.filterNot {
                it.group == "component" && it.subgroups == "hair-style" // 发型 4
            }.also {
                println("hair-style: ${list.size - it.size}, ${it.size}")
            }
        }
        .let { list ->
            list.filterNot {
                it.hexcode.endsWith("-200D-2640-FE0F") || // 女 50
                        it.hexcode.endsWith("-200D-2642-FE0F") // 男 50
            }.also {
                println("gender: ${list.size - it.size}, ${it.size}")
            }
        }
        .let { list ->
            list.filterNot {
                it.hexcode.startsWith("1F468-") || // 男 41
                        it.hexcode.startsWith("1F469-") // 女 38
            }.also {
                println("gender2: ${list.size - it.size}, ${it.size}")
            }
        }
        .also {
            val groupMap = linkedMapOf<String, MutableList<OpenMoji>>()
            it.forEach { openMoji ->
                groupMap[openMoji.group] = groupMap.getOrDefault(openMoji.group, mutableListOf()).also { list ->
                    list.add(openMoji)
                }
            }
            val groupList = groupMap.map { (key, value) ->
                key to value.size
            }
            println("groupList: $groupList")
        }
}

private fun writeJsonFiltered() {
    val openMoji2List = filter().map {
        OpenMoji2(
            it.emoji,
            it.hexcode,
            it.group,
            it.subgroups,
            it.annotation,
        )
    }
    File("lib/src/main/assets", "openmoji.json")
        .writeText(gsonPrettyPrinting.toJson(openMoji2List))
}
