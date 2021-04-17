package dev.ebnbin.openmojipicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

internal open class SpanSizeGridLayoutManagerViewModel : ViewModel() {
    internal val scrollPosition: MutableLiveData<Int> = MutableLiveData(0)

    internal val scrollOffset: MutableLiveData<Int> = MutableLiveData(0)

    internal val mutableIsLayoutFinished: MutableLiveData<Boolean> = MutableLiveData(false)

    val isLayoutFinished: LiveData<Boolean> = mutableIsLayoutFinished.map {
        it
    }
}
