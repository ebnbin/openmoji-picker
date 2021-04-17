package dev.ebnbin.openmojipicker.internal

import androidx.recyclerview.widget.DiffUtil
import dev.ebnbin.openmojipicker.OpenMoji
import dev.ebnbin.openmojipicker.OpenMojiGroup

internal data class OpenMojiPickerItem(
    val viewType: ViewType,
    val openMojiGroup: OpenMojiGroup? = null,
    val openMoji: OpenMoji? = null,
) {
    enum class ViewType {
        OPENMOJI_GROUP,
        OPENMOJI;

        companion object {
            fun of(viewType: Int): ViewType {
                return values()[viewType]
            }
        }
    }

    companion object {
        val diffCallback: DiffUtil.ItemCallback<OpenMojiPickerItem> by lazy {
            object : DiffUtil.ItemCallback<OpenMojiPickerItem>() {
                override fun areItemsTheSame(oldItem: OpenMojiPickerItem, newItem: OpenMojiPickerItem): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: OpenMojiPickerItem, newItem: OpenMojiPickerItem): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}
