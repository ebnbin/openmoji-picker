package dev.ebnbin.openmojipicker

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dev.ebnbin.eb.BindingViewHolder
import dev.ebnbin.eb.layoutInflater
import dev.ebnbin.eb.notNull
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemGroupBinding
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemOpenmojiBinding
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemSubgroupBinding

internal class OpenMojiPickerAdapter(
    private val viewModel: OpenMojiPickerViewModel,
    private val listener: Listener,
) : ListAdapter<OpenMojiPickerItem, BindingViewHolder<ViewBinding>>(
    object : DiffUtil.ItemCallback<OpenMojiPickerItem>() {
        override fun areItemsTheSame(oldItem: OpenMojiPickerItem, newItem: OpenMojiPickerItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OpenMojiPickerItem, newItem: OpenMojiPickerItem): Boolean {
            return oldItem == newItem
        }
    },
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewBinding> {
        val layoutInflater = parent.context.layoutInflater
        val binding = when (OpenMojiPickerItem.ViewType.of(viewType)) {
            OpenMojiPickerItem.ViewType.GROUP -> OpenmojiPickerItemGroupBinding.inflate(layoutInflater, parent, false)
            OpenMojiPickerItem.ViewType.SUBGROUP -> OpenmojiPickerItemSubgroupBinding.inflate(layoutInflater, parent, false)
            OpenMojiPickerItem.ViewType.OPENMOJI -> OpenmojiPickerItemOpenmojiBinding.inflate(layoutInflater, parent, false)
        }
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewBinding>, position: Int) {
        when (OpenMojiPickerItem.ViewType.of(getItemViewType(position))) {
            OpenMojiPickerItem.ViewType.GROUP -> Unit
            OpenMojiPickerItem.ViewType.SUBGROUP -> {
                val binding = holder.binding as OpenmojiPickerItemSubgroupBinding
                val subgroup = getItem(position).subgroup.notNull()
                binding.openmojiPickerChip.text = "${subgroup.openMojiSubgroup.group}#${subgroup.openMojiSubgroup.subgroup} (${subgroup.openMojiSubgroup.openMojiCount})"
            }
            OpenMojiPickerItem.ViewType.OPENMOJI -> {
                val binding = holder.binding as OpenmojiPickerItemOpenmojiBinding
                val openMoji = getItem(position).openMoji.notNull()
                Glide.with(binding.openmojiPickerEmoji)
                    .load(openMoji.drawableId)
                    .into(binding.openmojiPickerEmoji)
                binding.root.isSelected = position == viewModel.selectedPosition.value
                binding.openmojiPickerEmoji.setOnClickListener {
                    listener.openMojiOnClick(binding, openMoji, position)
                }
                binding.openmojiPickerEmoji.setOnLongClickListener {
                    listener.openMojiOnLongClick(binding, openMoji, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    interface Listener {
        fun openMojiOnClick(binding: OpenmojiPickerItemOpenmojiBinding, openMoji: OpenMoji, position: Int)

        fun openMojiOnLongClick(binding: OpenmojiPickerItemOpenmojiBinding, openMoji: OpenMoji, position: Int): Boolean
    }
}
