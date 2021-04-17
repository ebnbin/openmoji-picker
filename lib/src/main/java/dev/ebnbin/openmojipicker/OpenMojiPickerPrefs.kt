package dev.ebnbin.openmojipicker

import dev.ebnbin.eb.Pref
import dev.ebnbin.eb.Prefs
import dev.ebnbin.eb.appId

internal object OpenMojiPickerPrefs : Prefs() {
    override val prefName: String = "$appId.openmojipicker"

    val recentEnabled: Pref<Boolean> = createPref("recent_enabled", true)
    val recentList: Pref<String> = createPref("recent_list", "")
}
