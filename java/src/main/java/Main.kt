import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

fun main() {
//    json()
//    filter()
//    openMoji2List()
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
        it.hexcode.contains("1F9B0") || // 红发 3+1=4
                it.hexcode.contains("1F9B1") || // 卷发 3+1=4
                it.hexcode.contains("1F9B2") || // 光头 3+1=4
                it.hexcode.contains("1F9B3") || // 白发 3+1=4
                it.hexcode.contains("1F471") // 金发 3
    }
    println("hair-style: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.hexcode.contains("200D-2640-FE0F") || // 女 10+7+9+9+13+1+2=51
                it.hexcode.contains("200D-2642-FE0F") // 男 10+7+9+9+13+1+2=51
    }
    println("man woman: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.hexcode.contains("1F468") || // 男 1+17+3+19=40
                it.hexcode.contains("1F469") // 女 1+17+3+19=40, 重复 7, 男女共 73
    }
    println("man woman 2: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.hexcode == "1F466" || // 男孩
                it.hexcode == "1F467" || // 女孩
                it.hexcode == "1F474" || // 男老人
                it.hexcode == "1F475" || // 女老人
                it.hexcode == "1F46D" || // 女女握手
                it.hexcode == "1F46B" || // 女男握手
                it.hexcode == "1F46C" || // 男男握手
                it.hexcode == "E2C9" || // 男极地探险
                it.hexcode == "E2CA" || // 女极地探险
                it.hexcode == "E186" || // 男咖啡师
                it.hexcode == "E187" // 女咖啡师
    }
    println("man woman 3: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

    openMojiList = openMojiList.filterNot {
        it.hexcode == "1F5FE" || // 日本地图
                it.hexcode == "1F5FB" || // 富士山
                it.hexcode == "1F201" || // 日本文字按钮
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
                it.hexcode == "1F235" || //
                it.hexcode == "1F38C" // 交叉日本棋
    }
    println("japan: ${size - openMojiList.size}, ${openMojiList.size}")
    size = openMojiList.size

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
