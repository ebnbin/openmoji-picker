package dev.ebnbin.openmojipicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal open class SpanSizeGridLayoutManagerViewModel : ViewModel() {
    internal val scrollPosition: MutableLiveData<Int> = MutableLiveData(0)

    internal val scrollOffset: MutableLiveData<Int> = MutableLiveData(0)
}
