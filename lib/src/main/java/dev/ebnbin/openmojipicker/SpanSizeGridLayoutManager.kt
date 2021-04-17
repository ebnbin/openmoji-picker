package dev.ebnbin.openmojipicker

import android.content.Context
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ebnbin.eb.dpToPx
import dev.ebnbin.eb.notNull
import kotlin.math.max

/**
 * 根据 [spanSize] 自动调整 [mSpanCount]. 只支持 [mOrientation] 为 [RecyclerView.VERTICAL] 且 [mReverseLayout] 为 false.
 *
 * @param spanSize 最小网格宽度.
 */
internal open class SpanSizeGridLayoutManager(
    context: Context,
    val viewModel: SpanSizeGridLayoutManagerViewModel,
    val spanSize: Float,
) : GridLayoutManager(context, 1, VERTICAL, false) {
    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (orientation != VERTICAL || dy == 0) return
            val scrollPosition = findFirstVisibleItemPosition()
            viewModel.scrollPosition.value = scrollPosition
            viewModel.scrollOffset.value = findViewByPosition(scrollPosition)?.top ?: 0
        }
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        requireNotNull(view)
        val visibility = view.visibility
        view.isInvisible = true
        view.addOnScrollListener(onScrollListener)
        view.doOnLayout {
            val recyclerViewSize = if (orientation == VERTICAL) {
                view.width - view.paddingStart - view.paddingEnd
            } else {
                0
            }
            view.doOnNextLayout {
                view.visibility = visibility
            }
            spanCount = max(1, (recyclerViewSize / spanSize.dpToPx).toInt())
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
