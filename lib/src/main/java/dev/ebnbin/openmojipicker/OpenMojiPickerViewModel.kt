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

    private val openMojiMap: LiveData<Map<OpenMojiGroup, List<OpenMoji>>> = Transformations.map(openMojiList) {
        val map = linkedMapOf<String, MutableList<OpenMoji>>()
        openMojiList.value.notNull().forEach {
            map[it.group] = (map[it.group] ?: mutableListOf()).also { openMojiList ->
                openMojiList.add(it)
            }
        }
        map
            .mapKeys { groupEntry ->
                OpenMojiGroup(
                    group = groupEntry.key,
                    openMojiCount = groupEntry.value.size,
                    openMoji = groupEntry.value.first(),
                )
            }
    }

    val openMojiPickerItemList: LiveData<List<OpenMojiPickerItem>> = Transformations.map(openMojiMap) {
        val list = mutableListOf<OpenMojiPickerItem>()
        var index = 0
        openMojiMap.value.notNull().forEach { (openMojiGroup, openMojiList) ->
            list.add(
                OpenMojiPickerItem(
                    viewType = OpenMojiPickerItem.ViewType.GROUP,
                    group = OpenMojiPickerItem.Group(
                        openMojiGroup,
                        indexRange = index..(index + openMojiGroup.openMojiCount)
                    ),
                ),
            )
            ++index
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
        list
    }

    val openMojiGroupList: LiveData<List<OpenMojiPickerItem>> = Transformations.map(openMojiPickerItemList) {
        openMojiPickerItemList.value.notNull().filter { it.viewType == OpenMojiPickerItem.ViewType.GROUP }
    }

    val selectedPosition: MutableLiveData<Int?> = MutableLiveData(null)
}
