package dev.ebnbin.openmojipicker

import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.ebnbin.eb.app
import dev.ebnbin.eb.notNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OpenMojiViewModel : ViewModel() {
    val openMojiList: MutableLiveData<List<OpenMoji>> = MutableLiveData(emptyList())

    init {
        viewModelScope.launch {
            openMojiList.value = withContext(Dispatchers.IO) {
                Gson().fromJson<List<OpenMoji>>(
                    app.resources.openRawResource(R.raw.openmoji).bufferedReader(),
                    object : TypeToken<List<OpenMoji>>() {}.type,
                )
                    .filterNot { it.group == "flags" && (it.subgroups == "country-flag" || it.subgroups == "subdivision-flag") } // flags
                    .filterNot { it.group == "extras-openmoji" || it.group == "extras-unicode" } // extras
            }
        }
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
}
