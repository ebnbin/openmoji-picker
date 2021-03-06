package dev.ebnbin.openmojipicker

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import dev.ebnbin.ebui.FragmentWrapperActivity

/**
 * 需要 MaterialComponents 主题.
 */
class OpenMojiPickerActivity : FragmentWrapperActivity() {
    override val fragmentClass: Class<out Fragment> = OpenMojiPickerFragment::class.java

    companion object {
        const val KEY_OPENMOJI = "openmoji"

        fun createIntent(context: Context): Intent {
            return Intent()
                .setClass(context, OpenMojiPickerActivity::class.java)
        }
    }
}
