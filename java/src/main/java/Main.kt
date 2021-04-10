import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.Locale

fun main() {
//    json()
//    filter()
//    openMoji2List()
    filterCopy()
//    resourceName()
//    deleteDrawable()
//    drawableDp()
//    rename48()
//    png24()
}

private fun openMojiList(): List<OpenMoji> {
    val openMojiList = GsonBuilder().create().fromJson<List<OpenMoji>>(
        File("file", "openmoji.json").bufferedReader(),
        object : TypeToken<List<OpenMoji>>() {}.type,
    )
    return openMojiList
}

private fun json() {
    val openMojiList = openMojiList()
    println(openMojiList.size)
}

private fun filter(): List<OpenMoji> {
    var openMojiList = openMojiList()
    println(openMojiList.size)
    var size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.group == "extras-openmoji" // 317
    }
    println("extras-openmoji: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.group == "extras-unicode" // 57
    }
    println("extras-unicode: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filter {
        it.skintone.isEmpty() // 皮肤 1490
    }
    println("skintone: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.group == "component" && it.subgroups == "skin-tone" // 皮肤 5
    }
    println("skin-tone: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.group == "component" && it.subgroups == "hair-style" // 发型 4
    }
    println("hair-style: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

//    openMojiList = openMojiList.filterNot {
//        it.hexcode.contains("1F9B0") || // 红发 3+1=4
//                it.hexcode.contains("1F9B1") || // 卷发 3+1=4
//                it.hexcode.contains("1F9B2") || // 光头 3+1=4
//                it.hexcode.contains("1F9B3") || // 白发 3+1=4
//                it.hexcode.contains("1F471") // 金发 3
//    }
//    println("hair-style: ${size - openMojiList.size}, ${openMojiList.size}")
//    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.hexcode.contains("200D-2640-FE0F") || // 女 10+7+9+9+13+1=49+1(金发)=50
                it.hexcode.contains("200D-2642-FE0F") // 男 10+7+9+9+13+1=49+1(金发)=50
    }
    println("man woman: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.hexcode != "1F468" && it.hexcode.contains("1F468") || // 男 17+3+19=39+4(各种发型)=43
                it.hexcode != "1F469" && it.hexcode.contains("1F469") // 女 17+3+19=39+4(各种发型)=43, 重复 7, 男女共 79
    }
    println("man woman 2: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

//    openMojiList = openMojiList.filterNot {
//        it.hexcode == "1F466" || // 男孩
//                it.hexcode == "1F467" || // 女孩
//                it.hexcode == "1F474" || // 男老人
//                it.hexcode == "1F475" || // 女老人
//                it.hexcode == "1F46D" || // 女女握手
//                it.hexcode == "1F46B" || // 女男握手
//                it.hexcode == "1F46C" // 男男握手
//                it.hexcode == "E2C9" || // 男极地探险*
//                it.hexcode == "E2CA" || // 女极地探险*
//                it.hexcode == "E186" || // 男咖啡师*
//                it.hexcode == "E187" // 女咖啡师*
//    }
//    println("man woman 3: ${size - openMojiList.size}, ${openMojiList.size}")
//    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
//        it.hexcode == "1F5FE" || // 日本地图
                it.hexcode == "1F201" || // 日本文字按钮 17
                it.hexcode == "1F202" || //
                it.hexcode == "1F237" || //
                it.hexcode == "1F236" || //
                it.hexcode == "1F22F" || //
                it.hexcode == "1F250" || //
                it.hexcode == "1F239" || //
                it.hexcode == "1F21A" || //
                it.hexcode == "1F232" || //
                it.hexcode == "1F251" || //
                it.hexcode == "1F238" || //
                it.hexcode == "1F234" || //
                it.hexcode == "1F233" || //
                it.hexcode == "3297" || //
                it.hexcode == "3299" || //
                it.hexcode == "1F23A" || //
                it.hexcode == "1F235" //
    }
    println("japan: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    val map = mutableMapOf<String, Int>()
    openMojiList.forEach {
        map[it.group] = map.getOrDefault(it.group, 0) + 1
    }
    println(map)

    return openMojiList
}

private fun openMoji2List() {
    val openMoji2List = mutableListOf<OpenMoji2>()
    filter().forEach {
        openMoji2List.add(
            OpenMoji2(
                it.emoji,
                it.hexcode,
                it.group,
                it.subgroups,
                it.annotation,
            ),
        )
    }
    File("file", "openmoji2.json").writeText(GsonBuilder().setPrettyPrinting().create().toJson(openMoji2List))
}

private fun filterCopy() {
    val filterList = filter()
    File("file/xml").listFiles()!!
        .filter { xmlFile ->
            filterList
                .map { "openmoji_${it.hexcode.toLowerCase(Locale.ROOT).replace(Regex("[^0-9_a-z]"), "_")}.xml" }
                .contains(xmlFile.name)
        }
        .forEach {
            it.copyTo(File("lib/src/main/res-openmoji/drawable", it.name), overwrite = true)
        }
    File("file/72x72").listFiles()!!
        .filter { xmlFile ->
            filterList
                .map { "${it.hexcode}.png" }
                .contains(xmlFile.name)
        }
        .forEach {
            it.copyTo(File("lib/src/main/res-openmoji/drawable-xxhdpi", "openmoji_24_${it.nameWithoutExtension.toLowerCase(Locale.ROOT).replace(Regex("[^0-9_a-z]"), "_")}.png"), overwrite = true)
        }
}

private fun resourceName() {
    File("lib/src/main/res-openmoji/drawable").listFiles()!!.forEach {
        it.writeText(it.readText().replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<vector\n" +
                "    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:width=\"72dp\"\n" +
                "    android:height=\"72dp\"\n" +
                "    android:viewportWidth=\"72.0\"\n" +
                "    android:viewportHeight=\"72.0\">\n", "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<vector\n" +
                "    xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    android:width=\"72dp\"\n" +
                "    android:height=\"72dp\"\n" +
                "    android:viewportWidth=\"72.0\"\n" +
                "    android:viewportHeight=\"72.0\"\n" +
                "    tools:ignore=\"ResourceName\">\n"))
    }
}

private fun deleteDrawable() {
    var openMojiNameList = mutableListOf<String>()
    openMojiNameList.addAll(
        filter()
            .map { "openmoji_${it.hexcode.toLowerCase(Locale.ROOT).replace("-", "_")}.xml" },
    )
    openMojiNameList.addAll(
        filter()
            .map { "openmoji_48_${it.hexcode.toLowerCase(Locale.ROOT).replace("-", "_")}.xml" },
    )
    File("lib/src/main/res-openmoji/drawable").listFiles()!!
        .filterNot { openMojiNameList.contains(it.name) }
        .forEach {
            it.delete()
        }

    openMojiNameList = filter()
        .map { "openmoji_24_${it.hexcode.toLowerCase(Locale.ROOT).replace("-", "_")}.png" }
        .toMutableList()
    File("lib/src/main/res-openmoji/drawable-xxhdpi").listFiles()!!
        .filterNot { openMojiNameList.contains(it.name) }
        .forEach {
            it.delete()
        }
}

private fun drawableDp() {
    val from = 72
    val to = 48
    File("lib/src/main/res-openmoji/drawable").listFiles()!!
        .filter { it.name.startsWith("openmoji_") && !it.name.startsWith("openmoji_icon_") }
        .forEach {
            val text = it.readText()
                .replace("android:width=\"${from}dp\"", "android:width=\"${to}dp\"")
                .replace("android:height=\"${from}dp\"", "android:height=\"${to}dp\"")
            it.writeText(text)
        }
}

private fun rename48() {
    File("lib/src/main/res-openmoji/drawable").listFiles()!!
        .filter { it.name.startsWith("openmoji_") && !it.name.startsWith("openmoji_icon_") }
        .forEach {
            it.renameTo(
                File(
                    it.parent,
                    "openmoji_48_${it.nameWithoutExtension.substring(9)}.xml",
                )
            )
        }
}

private fun png24() {
    val openMojiNameList = filter()
        .map { "${it.hexcode}.png" }
    File("file/72x72").listFiles()!!
        .filter { openMojiNameList.contains(it.name) }
        .forEach {
            val file = File(
                "lib/src/main/res-openmoji/drawable-xxhdpi",
                "openmoji_24_${it.nameWithoutExtension.toLowerCase(Locale.ROOT).replace("-", "_")}.png",
            )
            it.copyTo(file)
        }
}
