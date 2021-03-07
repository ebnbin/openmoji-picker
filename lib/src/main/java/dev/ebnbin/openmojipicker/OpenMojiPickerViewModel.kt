package dev.ebnbin.openmojipicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.ebnbin.eb.notNull

internal class OpenMojiPickerViewModel(private val openMojiViewModel: OpenMojiViewModel) : ViewModel() {
    class Factory(private val openMojiViewModel: OpenMojiViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return OpenMojiPickerViewModel(openMojiViewModel) as T
        }
    }

    val openMojiPickerItemList: LiveData<List<OpenMojiPickerItem>> = Transformations.map(openMojiViewModel.openMojiMap) {
        val list = mutableListOf<OpenMojiPickerItem>()
        var index = 0
        openMojiViewModel.openMojiMap.value.notNull().forEach { (openMojiGroup, openMojiList) ->
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
