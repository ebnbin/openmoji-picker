package dev.ebnbin.openmojipicker

import dev.ebnbin.eb.Pref
import dev.ebnbin.eb.appId

internal object OpenMojiPickerPrefs {
    private val name: String = "$appId.openmojipicker"

    val enable_recent: Pref<Boolean> = Pref.create("enable_recent", true, name)
    val recent: Pref<String> = Pref.create("recent", "", name)
}
