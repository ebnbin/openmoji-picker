package dev.ebnbin.openmojipicker

import androidx.fragment.app.Fragment
import dev.ebnbin.ebui.FragmentWrapperActivity

/**
 * 需要 MaterialComponents 主题.
 */
class OpenMojiPickerActivity : FragmentWrapperActivity() {
    override val fragmentClass: Class<out Fragment> = OpenMojiPickerFragment::class.java
}
