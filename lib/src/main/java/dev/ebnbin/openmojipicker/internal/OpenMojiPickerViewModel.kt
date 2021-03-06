package dev.ebnbin.openmojipicker.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dev.ebnbin.eb.notNull
import dev.ebnbin.openmojipicker.OpenMoji
import dev.ebnbin.openmojipicker.OpenMojiGroup

internal class OpenMojiPickerViewModel : ViewModel() {
    private val dataMap: LiveData<out Map<OpenMojiGroup, List<OpenMoji>>> =
        MediatorLiveData<MutableMap<OpenMojiGroup, MutableList<OpenMoji>>>().also {
            val value = linkedMapOf<OpenMojiGroup, MutableList<OpenMoji>>()
            value[OpenMojiGroup.RECENT] = mutableListOf()
            OpenMoji.allList.forEach { openMoji ->
                val openMojiGroup = openMoji.getOpenMojiGroup()
                value[openMojiGroup] = (value[openMojiGroup] ?: mutableListOf()).also { openMojiList ->
                    openMojiList.add(openMoji)
                }
            }
            it.value = value

            it.addSource(OpenMojiPickerPrefs.recentList) { recentList ->
                it.value = it.value.notNull().also { map ->
                    map[OpenMojiGroup.RECENT] = if (recentList.isEmpty()) {
                        mutableListOf()
                    } else {
                        recentList
                            .split(",")
                            .mapTo(mutableListOf()) { hexcode -> OpenMoji.allMap.getValue(hexcode) }
                    }
                }
            }
        }

    val itemList: LiveData<List<OpenMojiPickerItem>> = dataMap.map {
        it
            .filterNot { (_, openMojiList) ->
                openMojiList.isEmpty()
            }
            .flatMap { (openMojiGroup, openMojiList) ->
                listOf(
                    OpenMojiPickerItem(
                        viewType = OpenMojiPickerItem.ViewType.OPENMOJI_GROUP,
                        openMojiGroup = openMojiGroup,
                    ),
                ) + openMojiList.map { openMoji ->
                    OpenMojiPickerItem(
                        viewType = OpenMojiPickerItem.ViewType.OPENMOJI,
                        openMoji = openMoji,
                    )
                }
            }
    }

    val saveRecent: LiveData<Boolean> = OpenMojiPickerPrefs.saveRecent.map {
        it
    }

    fun toggleSaveRecent() {
        if (OpenMojiPickerPrefs.saveRecent.value) {
            clearRecent()
        }
        OpenMojiPickerPrefs.saveRecent.value = !OpenMojiPickerPrefs.saveRecent.value
    }

    val hasRecent: LiveData<Boolean> = OpenMojiPickerPrefs.recentList.map {
        it.isNotEmpty()
    }

    fun saveRecent(openMoji: OpenMoji) {
        if (!OpenMojiPickerPrefs.saveRecent.value) {
            return
        }
        val recentList = OpenMojiPickerPrefs.recentList.value
        val list = mutableListOf<String>()
        if (recentList.isNotEmpty()) {
            list.addAll(recentList.split(","))
        }
        list.remove(openMoji.hexcode)
        list.add(0, openMoji.hexcode)
        OpenMojiPickerPrefs.recentList.value = list.take(RECENT_MAX).joinToString(",")
    }

    fun clearRecent() {
        OpenMojiPickerPrefs.recentList.value = ""
    }

    companion object {
        private const val RECENT_MAX = 20
    }
}
