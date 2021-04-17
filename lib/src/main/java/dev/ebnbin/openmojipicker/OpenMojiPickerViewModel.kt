package dev.ebnbin.openmojipicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ebnbin.eb.notNull
import kotlinx.coroutines.launch

internal class OpenMojiPickerViewModel : ViewModel() {
    private val dataMap: MutableLiveData<Map<OpenMojiGroup, List<OpenMoji>>> = MutableLiveData(mapOf())

    init {
        viewModelScope.launch {
            loadDataMap()
        }
    }

    private fun loadDataMap() {
        val map = linkedMapOf<OpenMojiGroup, MutableList<OpenMoji>>()
        val recent = OpenMojiPickerPrefs.recentList.value
        if (recent.isNotEmpty()) {
            recent
                .split(",")
                .forEach { hexcode ->
                    map[OpenMojiGroup.RECENT] = (map[OpenMojiGroup.RECENT] ?: mutableListOf()).also {
                        it.add(OpenMoji.allMap.getValue(hexcode))
                    }
                }

        }
        OpenMoji.allList.forEach { openMoji ->
            val openMojiGroup = OpenMojiGroup.of(openMoji.group)
            map[openMojiGroup] = (map[openMojiGroup] ?: mutableListOf()).also {
                it.add(openMoji)
            }
        }
        dataMap.value = map
    }

    val itemList: LiveData<List<OpenMojiPickerItem>> = Transformations.map(dataMap) {
        val list = mutableListOf<OpenMojiPickerItem>()
        dataMap.value.notNull().forEach { (openMojiGroup, openMojiList) ->
            list.add(
                OpenMojiPickerItem(
                    viewType = OpenMojiPickerItem.ViewType.OPENMOJI_GROUP,
                    openMojiGroup = openMojiGroup,
                ),
            )
            openMojiList.forEach { openMoji ->
                list.add(
                    OpenMojiPickerItem(
                        viewType = OpenMojiPickerItem.ViewType.OPENMOJI,
                        openMoji = openMoji,
                    ),
                )
            }
        }
        list
    }

    fun saveRecent(openMoji: OpenMoji) {
        val recent = OpenMojiPickerPrefs.recentList.value
        val list = mutableListOf<String>()
        if (recent.isNotEmpty()) {
            list.addAll(recent.split(","))
        }
        list.remove(openMoji.hexcode)
        list.add(0, openMoji.hexcode)
        OpenMojiPickerPrefs.recentList.value = list.take(RECENT_MAX).joinToString(",")
    }

    fun clearRecent() {
        OpenMojiPickerPrefs.recentList.value = ""
        loadDataMap()
    }

    companion object {
        private const val RECENT_MAX = 20
    }
}
