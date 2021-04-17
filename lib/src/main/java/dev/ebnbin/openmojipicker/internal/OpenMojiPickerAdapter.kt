package dev.ebnbin.openmojipicker.internal

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dev.ebnbin.eb.layoutInflater
import dev.ebnbin.eb.notNull
import dev.ebnbin.ebui.BindingViewHolder
import dev.ebnbin.openmojipicker.OpenMoji
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemOpenmojiBinding
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemOpenmojiGroupBinding

internal class OpenMojiPickerAdapter(private val listener: Listener) :
    ListAdapter<OpenMojiPickerItem, BindingViewHolder<ViewBinding>>(OpenMojiPickerItem.diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewBinding> {
        val layoutInflater = parent.context.layoutInflater
        val binding = when (OpenMojiPickerItem.ViewType.of(viewType)) {
            OpenMojiPickerItem.ViewType.OPENMOJI_GROUP ->
                OpenmojiPickerItemOpenmojiGroupBinding.inflate(layoutInflater, parent, false)
            OpenMojiPickerItem.ViewType.OPENMOJI ->
                OpenmojiPickerItemOpenmojiBinding.inflate(layoutInflater, parent, false)
        }
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewBinding>, position: Int) {
        when (OpenMojiPickerItem.ViewType.of(getItemViewType(position))) {
            OpenMojiPickerItem.ViewType.OPENMOJI_GROUP -> {
                val binding = holder.binding as OpenmojiPickerItemOpenmojiGroupBinding
                val openMojiGroup = getItem(position).openMojiGroup.notNull()
                binding.openmojiPickerChip.setText(openMojiGroup.stringId)
            }
            OpenMojiPickerItem.ViewType.OPENMOJI -> {
                val binding = holder.binding as OpenmojiPickerItemOpenmojiBinding
                val openMoji = getItem(position).openMoji.notNull()
                Glide.with(binding.openmojiPickerEmoji)
                    .load(openMoji.getDrawableId24())
                    .into(binding.openmojiPickerEmoji)
                binding.root.setOnClickListener {
                    listener.openMojiOnClick(binding, openMoji, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    interface Listener {
        fun openMojiOnClick(binding: OpenmojiPickerItemOpenmojiBinding, openMoji: OpenMoji, position: Int)
    }
}
