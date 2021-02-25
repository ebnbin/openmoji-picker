package dev.ebnbin.openmojipicker

import android.content.Context
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ebnbin.eb.dpToPx
import dev.ebnbin.eb.notNull
import kotlin.math.max

internal class OpenMojiPickerLayoutManager(
    context: Context,
    private val viewModel: OpenMojiPickerViewModel,
) : GridLayoutManager(context, 1) {
    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy == 0) return
            val scrollPosition = findFirstVisibleItemPosition()
            viewModel.scrollPosition.value = scrollPosition
            viewModel.scrollOffset.value = findViewByPosition(scrollPosition)?.top ?: 0
        }
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        requireNotNull(view)
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = view.adapter.notNull()
                return when (OpenMojiPickerItem.ViewType.of(adapter.getItemViewType(position))) {
                    OpenMojiPickerItem.ViewType.GROUP -> spanCount
                    OpenMojiPickerItem.ViewType.EMOJI -> 1
                }
            }
        }
        view.addOnScrollListener(onScrollListener)
        view.doOnLayout {
            val minItemWidth = 72f.dpToPx
            val recyclerViewWidth = view.width - view.paddingStart - view.paddingEnd
            val spanCount = (recyclerViewWidth / minItemWidth).toInt()
            this.spanCount = max(1, spanCount)
            val scrollPosition = viewModel.scrollPosition.value.notNull()
            val scrollOffset = viewModel.scrollOffset.value.notNull()
            scrollToPositionWithOffset(scrollPosition, scrollOffset)
        }
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        requireNotNull(view)
        view.removeOnScrollListener(onScrollListener)
        super.onDetachedFromWindow(view, recycler)
    }
}
