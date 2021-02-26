package dev.ebnbin.openmojipicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerFragmentBinding
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemEmojiBinding

class OpenMojiPickerFragment : Fragment(), OpenMojiPickerAdapter.Listener {
    private val viewModel: OpenMojiPickerViewModel by viewModels()

    private val spanSizeGridLayoutManagerViewModel: SpanSizeGridLayoutManagerViewModel by viewModels()

    private lateinit var binding: OpenmojiPickerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = OpenmojiPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy == 0) return
            val selectedPosition = viewModel.selectedPosition.value ?: return
            if (selectedPosition < layoutManager.findFirstCompletelyVisibleItemPosition() ||
                selectedPosition > layoutManager.findLastCompletelyVisibleItemPosition()) {
                viewModel.selectedPosition.value = null
                adapter.notifyItemChanged(selectedPosition)
            }
        }
    }

    private lateinit var layoutManager: OpenMojiPickerLayoutManager

    private lateinit var adapter: OpenMojiPickerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = OpenMojiPickerLayoutManager(requireContext(), spanSizeGridLayoutManagerViewModel)
        adapter = OpenMojiPickerAdapter(viewModel, this)
        adapter.submitList(viewModel.openMojiPickerItemList)

        viewModel.selectedPosition.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.openmojiPickerExtendedFloatingActionButton.hide()
            } else {
                adapter.notifyItemChanged(it)
                binding.openmojiPickerExtendedFloatingActionButton.show()
            }
        }

        binding.openmojiPickerRecyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addOnScrollListener(onScrollListener)
        }

        if (viewModel.selectedPosition.value == null) {
            binding.openmojiPickerExtendedFloatingActionButton.hide()
        } else {
            binding.openmojiPickerExtendedFloatingActionButton.show()
        }
    }

    override fun onDestroyView() {
        binding.openmojiPickerRecyclerView.removeOnScrollListener(onScrollListener)
        super.onDestroyView()
    }

    override fun emojiOnClick(binding: OpenmojiPickerItemEmojiBinding, openMoji: OpenMoji, position: Int) {
        val selectedPosition = viewModel.selectedPosition.value
        if (selectedPosition == position) {
            viewModel.selectedPosition.value = null
            adapter.notifyItemChanged(position)
        } else {
            viewModel.selectedPosition.value = position
            if (selectedPosition != null) {
                adapter.notifyItemChanged(selectedPosition)
            }
        }
    }
}
