import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

fun main() {
//    json()
}

private fun json() {
    val openMojiList: List<OpenMoji> = Gson().fromJson(
        File("file", "openmoji.json").bufferedReader(),
        object : TypeToken<List<OpenMoji>>() {}.type,
    )
    println(openMojiList.size)
}
