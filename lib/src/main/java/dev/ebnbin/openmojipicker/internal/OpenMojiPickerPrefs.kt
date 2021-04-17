package dev.ebnbin.openmojipicker.internal

import dev.ebnbin.eb.Pref
import dev.ebnbin.eb.Prefs
import dev.ebnbin.eb.appId

internal object OpenMojiPickerPrefs : Prefs() {
    override val prefName: String = "$appId.openmojipicker"

    val saveRecent: Pref<Boolean> = createPref("save_recent", true)
    val recentList: Pref<String> = createPref("recent_list", "")
}
