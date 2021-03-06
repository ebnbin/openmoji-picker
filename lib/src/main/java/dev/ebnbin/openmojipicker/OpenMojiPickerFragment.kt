package dev.ebnbin.openmojipicker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dev.ebnbin.eb.notNull
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerFragmentBinding
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemOpenmojiBinding

internal class OpenMojiPickerFragment : Fragment(),
    OpenMojiPickerAdapter.Listener,
    AdapterView.OnItemSelectedListener,
    TextWatcher {
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
            val selectedPosition = viewModel.selectedPosition.value
            if (selectedPosition != null) {
                if (selectedPosition < layoutManager.findFirstCompletelyVisibleItemPosition() ||
                    selectedPosition > layoutManager.findLastCompletelyVisibleItemPosition()
                ) {
                    viewModel.selectedPosition.value = null
                    adapter.notifyItemChanged(selectedPosition)
                }
            }
            val spinnerItem = viewModel.openMojiGroupList.value.notNull().firstOrNull {
                layoutManager.findFirstVisibleItemPosition() in it.group.notNull().indexRange
            }
            if (spinnerItem != null) {
                val index = viewModel.openMojiGroupList.value.notNull().indexOf(spinnerItem)
                if (index != -1 && index != binding.openmojiPickerSpinner.selectedItemPosition) {
                    byScroll = true
                    binding.openmojiPickerSpinner.setSelection(index)
                }
            }
        }
    }

    private var byScroll: Boolean = false

    private lateinit var layoutManager: OpenMojiPickerLayoutManager

    private lateinit var adapter: OpenMojiPickerAdapter

    private lateinit var spinnerAdapter: OpenMojiPickerSpinnerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = OpenMojiPickerLayoutManager(requireContext(), spanSizeGridLayoutManagerViewModel)
        layoutManager.listener = object : SpanSizeGridLayoutManager.Listener {
            override fun onLayoutFinish() {
                binding.openmojiPickerSpinner.onItemSelectedListener = this@OpenMojiPickerFragment
            }
        }
        adapter = OpenMojiPickerAdapter(viewModel, this)
        viewModel.openMojiPickerItemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.openmojiPickerCount.text = it
                .filter { openMojiPickerItem -> openMojiPickerItem.viewType == OpenMojiPickerItem.ViewType.OPENMOJI }
                .size.toString()

        }

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
        viewModel.openMojiGroupList.observe(viewLifecycleOwner) {
            spinnerAdapter = OpenMojiPickerSpinnerAdapter(requireContext(), it)
            binding.openmojiPickerSpinner.adapter = spinnerAdapter
        }
        binding.openmojiPickerFilter.addTextChangedListener(this)

        if (viewModel.selectedPosition.value == null) {
            binding.openmojiPickerExtendedFloatingActionButton.hide()
        } else {
            binding.openmojiPickerExtendedFloatingActionButton.show()
        }

        viewModel.filter()
    }

    override fun onDestroyView() {
        binding.openmojiPickerFilter.removeTextChangedListener(this)
        binding.openmojiPickerRecyclerView.removeOnScrollListener(onScrollListener)
        super.onDestroyView()
    }

    override fun openMojiOnClick(binding: OpenmojiPickerItemOpenmojiBinding, openMoji: OpenMoji, position: Int) {
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

    override fun openMojiOnLongClick(
        binding: OpenmojiPickerItemOpenmojiBinding,
        openMoji: OpenMoji,
        position: Int
    ): Boolean {
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (byScroll) {
            byScroll = false
            return
        }
        val index = viewModel.openMojiPickerItemList.value.notNull().indexOf(spinnerAdapter.getItem(position))
        layoutManager.scrollToPositionWithOffset(index, 0)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private var filterRunnable: Runnable? = null

    override fun afterTextChanged(s: Editable?) {
        filterRunnable?.let {
            filterRunnable = null
            binding.openmojiPickerFilter.removeCallbacks(it)
        }
        Runnable {
            viewModel.filter(s.toString())
        }.let {
            binding.openmojiPickerFilter.postDelayed(1000L) {
                it.run()
            }
            filterRunnable = it
        }
    }
}
