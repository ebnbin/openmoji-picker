package dev.ebnbin.openmojipicker

import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.ebnbin.eb.app
import dev.ebnbin.eb.notNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class OpenMojiPickerViewModel : ViewModel() {
    private val openMojiList: MutableLiveData<List<OpenMoji>> = MutableLiveData(emptyList())

    fun filter(text: String = "") {
        viewModelScope.launch {
            openMojiList.value = withContext(Dispatchers.IO) {
                Gson().fromJson<List<OpenMoji>>(
                    app.resources.openRawResource(R.raw.openmoji).bufferedReader(),
                    object : TypeToken<List<OpenMoji>>() {}.type,
                )
//                    .filterNot { it.group == "flags" && (it.subgroups == "country-flag" || it.subgroups == "subdivision-flag") } // flags
                    .filterNot { it.group == "extras-openmoji" || it.group == "extras-unicode" } // extras
                    .filter {
                        if (text.isEmpty()) {
                            true
                        } else {
                            it.emoji == text ||
                                    it.hexcode.contains(text, ignoreCase = true) ||
                                    it.group.contains(text, ignoreCase = true) ||
                                    it.subgroups.contains(text, ignoreCase = true) ||
                                    it.annotation.contains(text, ignoreCase = true)
                        }
                    }
            }
        }
    }

    private val openMojiMap: LiveData<Map<OpenMojiGroup, Map<OpenMojiSubgroup, List<OpenMoji>>>> = Transformations.map(openMojiList) {
        val map = linkedMapOf<String, LinkedHashMap<String, MutableList<OpenMoji>>>()
        openMojiList.value.notNull().forEach {
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
                    openMoji = groupEntry.value.values.first().first(),
                )
            }
            .mapValues { groupEntry ->
                groupEntry.value.mapKeys { subgroupEntry ->
                    OpenMojiSubgroup(
                        group = groupEntry.key.group,
                        subgroup = subgroupEntry.key,
                        openMojiCount = subgroupEntry.value.size,
                        openMoji = subgroupEntry.value.first(),
                    )
                }
            }
    }

    val openMojiPickerItemList: LiveData<List<OpenMojiPickerItem>> = Transformations.map(openMojiMap) {
        val list = mutableListOf<OpenMojiPickerItem>()
        var index = 0
        openMojiMap.value.notNull().forEach { (openMojiGroup, openMojiSubgroupMap) ->
            list.add(
                OpenMojiPickerItem(
                    viewType = OpenMojiPickerItem.ViewType.GROUP,
                    group = OpenMojiPickerItem.Group(
                        openMojiGroup,
                        indexRange = index..(index + openMojiGroup.subgroupCount + openMojiGroup.openMojiCount)
                    ),
                ),
            )
            ++index
            openMojiSubgroupMap.forEach { (openMojiSubgroup, openMojiList) ->
//                list.add(
//                    OpenMojiPickerItem(
//                        viewType = OpenMojiPickerItem.ViewType.SUBGROUP,
//                        subgroup = OpenMojiPickerItem.Subgroup(
//                            openMojiSubgroup,
//                            indexRange = index..(index + openMojiSubgroup.openMojiCount)
//                        ),
//                    ),
//                )
//                ++index
                openMojiList.forEach { openMoji ->
                    list.add(
                        OpenMojiPickerItem(
                            viewType = OpenMojiPickerItem.ViewType.OPENMOJI,
                            openMoji = openMoji
                        ),
                    )
                    ++index
                }
            }
        }
        list
    }

    val openMojiGroupList: LiveData<List<OpenMojiPickerItem>> = Transformations.map(openMojiPickerItemList) {
        openMojiPickerItemList.value.notNull().filter { it.viewType == OpenMojiPickerItem.ViewType.GROUP }
    }

    val selectedPosition: MutableLiveData<Int?> = MutableLiveData(null)
}
