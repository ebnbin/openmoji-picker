package dev.ebnbin.openmojipicker

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.ebnbin.eb.app

internal class OpenMojiPickerViewModel : ViewModel() {
    private val openMojiList: List<OpenMoji> by lazy {
        Gson().fromJson(
            app.resources.openRawResource(R.raw.openmoji).bufferedReader(),
            object : TypeToken<List<OpenMoji>>() {}.type,
        )
    }

    val openMojiPickerItemList: List<OpenMojiPickerItem> by lazy {
        val map = linkedMapOf<String, MutableList<OpenMoji>>()
        openMojiList.forEach {
            val group = "${it.group},${it.subgroups}"
            map[group] = (map.getOrDefault(group, mutableListOf())).also { openMojiList -> openMojiList.add(it) }
        }
        val list = mutableListOf<OpenMojiPickerItem>()
        map.forEach { (group, openMojiList) ->
            list.add(
                OpenMojiPickerItem(
                    viewType = OpenMojiPickerItem.ViewType.GROUP,
                    group = "$group,${openMojiList.size}",
                ),
            )
            openMojiList.forEach {
                list.add(
                    OpenMojiPickerItem(
                        viewType = OpenMojiPickerItem.ViewType.EMOJI,
                        openMoji = it,
                    ),
                )
            }
        }
        list
    }
}
