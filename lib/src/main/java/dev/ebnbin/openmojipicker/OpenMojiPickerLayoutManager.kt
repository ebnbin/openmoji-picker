package dev.ebnbin.openmojipicker

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ebnbin.eb.notNull

internal class OpenMojiPickerLayoutManager(
    context: Context,
    viewModel: SpanSizeGridLayoutManagerViewModel,
) : SpanSizeGridLayoutManager(
    context,
    viewModel,
    spanSize = 48f,
) {
    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        requireNotNull(view)
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = view.adapter.notNull()
                return when (OpenMojiPickerItem.ViewType.of(adapter.getItemViewType(position))) {
                    OpenMojiPickerItem.ViewType.OPENMOJI_GROUP -> spanCount
                    OpenMojiPickerItem.ViewType.OPENMOJI -> 1
                }
            }
        }
    }
}
