package dev.ebnbin.openmojipicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dev.ebnbin.eb.notNull

internal class OpenMojiPickerViewModel : ViewModel() {
    val openMojiList: MutableLiveData<List<OpenMoji>> = MutableLiveData(emptyList())

    init {
        openMojiList.value = OpenMoji.allList
    }

    val openMojiMap: LiveData<Map<OpenMojiGroup, List<OpenMoji>>> = Transformations.map(openMojiList) {
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
