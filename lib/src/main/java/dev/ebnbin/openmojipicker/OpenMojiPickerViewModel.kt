package dev.ebnbin.openmojipicker

import androidx.lifecycle.MutableLiveData
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

    private val openMojiMap: Map<OpenMojiGroup, Map<OpenMojiSubgroup, List<OpenMoji>>> by lazy {
        val map = linkedMapOf<String, LinkedHashMap<String, MutableList<OpenMoji>>>()
        openMojiList.forEach {
            map[it.group] = (map[it.group] ?: linkedMapOf()).also { subgroupMap ->
                subgroupMap[it.subgroups] = (subgroupMap[it.subgroups] ?: mutableListOf()).also { openMojiList ->
                    openMojiList.add(it)
                }
            }
        }
        map
            .mapKeys { groupEntry ->
                OpenMojiGroup(
                    group = groupEntry.key,
                    subgroupCount = groupEntry.value.size,
                    openMojiCount = groupEntry.value.asSequence().fold(0) { acc, entry -> acc + entry.value.size },
                )
            }
            .mapValues { groupEntry ->
                groupEntry.value.mapKeys { subgroupEntry ->
                    OpenMojiSubgroup(
                        group = groupEntry.key.group,
                        subgroup = subgroupEntry.key,
                        openMojiCount = subgroupEntry.value.size,
                    )
                }
            }
    }

    val openMojiPickerItemList: List<OpenMojiPickerItem> by lazy {
        val list = mutableListOf<OpenMojiPickerItem>()
        openMojiMap.forEach { (_, openMojiSubgroupMap) ->
            openMojiSubgroupMap.forEach { (openMojiSubgroup, openMojiList) ->
                list.add(
                    OpenMojiPickerItem(
                        viewType = OpenMojiPickerItem.ViewType.GROUP,
                        group = openMojiSubgroup,
                    ),
                )
                openMojiList.forEach { openMoji ->
                    list.add(
                        OpenMojiPickerItem(
                            viewType = OpenMojiPickerItem.ViewType.EMOJI,
                            openMoji = openMoji
                        ),
                    )
                }
            }
        }
        list
    }

    val openMojiGroupList: List<OpenMojiGroup> by lazy {
        openMojiMap.keys.toList()
    }

    val selectedPosition: MutableLiveData<Int?> = MutableLiveData(null)
}
