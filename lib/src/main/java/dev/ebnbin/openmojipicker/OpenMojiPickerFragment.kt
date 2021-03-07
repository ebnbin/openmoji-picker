package dev.ebnbin.openmojipicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dev.ebnbin.eb.notNull
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerFragmentBinding
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemOpenmojiBinding

internal class OpenMojiPickerFragment : Fragment(), OpenMojiPickerAdapter.Listener {
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
        }
    }

    private lateinit var layoutManager: OpenMojiPickerLayoutManager

    private lateinit var adapter: OpenMojiPickerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = OpenMojiPickerLayoutManager(requireContext(), spanSizeGridLayoutManagerViewModel)
        adapter = OpenMojiPickerAdapter(viewModel, this)
        viewModel.openMojiPickerItemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.selectedPosition.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.openmojiPickerPick.hide()
            } else {
                adapter.notifyItemChanged(it)
                binding.openmojiPickerPick.show()
            }
        }

        binding.openmojiPickerToolbar.let {
            it.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        binding.openmojiPickerRecyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addOnScrollListener(onScrollListener)
            it.addItemDecoration(
                HeaderItemDecoration(it) { itemPosition ->
                    viewModel.openMojiPickerItemList.value.notNull()[itemPosition].viewType == OpenMojiPickerItem.ViewType.GROUP
                },
            )
        }
        binding.openmojiPickerPick.let {
            it.setOnClickListener {
                val selectedPosition = viewModel.selectedPosition.value ?: return@setOnClickListener
                val openMoji = viewModel.openMojiPickerItemList.value.notNull()[selectedPosition].openMoji.notNull()
                requireActivity().setResult(
                    Activity.RESULT_OK,
                    Intent().putExtras(
                        bundleOf(
                            OpenMojiPickerActivity.KEY_OPENMOJI to openMoji
                        ),
                    ),
                )
                requireActivity().finish()
            }
        }

        if (viewModel.selectedPosition.value == null) {
            binding.openmojiPickerPick.hide()
        } else {
            binding.openmojiPickerPick.show()
        }
    }

    override fun onDestroyView() {
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
}
