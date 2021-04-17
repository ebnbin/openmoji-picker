package dev.ebnbin.openmojipicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.ebnbin.eb.notNull
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerFragmentBinding
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerItemOpenmojiBinding
import dev.ebnbin.openmojipicker.internal.HeaderItemDecoration
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerAdapter
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerItem
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerLayoutManager
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerViewModel
import dev.ebnbin.openmojipicker.internal.SpanSizeGridLayoutManagerViewModel

class OpenMojiPickerFragment : Fragment(), OpenMojiPickerAdapter.Listener {
    private val viewModel: OpenMojiPickerViewModel by viewModels()

    private val spanSizeGridLayoutManagerViewModel: SpanSizeGridLayoutManagerViewModel by viewModels()

    private lateinit var binding: OpenmojiPickerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = OpenmojiPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openmojiPickerRecyclerView.let {
            val layoutManager = OpenMojiPickerLayoutManager(requireContext(), spanSizeGridLayoutManagerViewModel)
            it.layoutManager = layoutManager

            val adapter = OpenMojiPickerAdapter(this)
            viewModel.itemList.observe(viewLifecycleOwner) { itemList ->
                adapter.submitList(itemList)
            }
            it.adapter = adapter

            val itemDecoration = HeaderItemDecoration(it) { itemPosition ->
                viewModel.itemList.value.notNull()[itemPosition].viewType == OpenMojiPickerItem.ViewType.OPENMOJI_GROUP
            }
            it.addItemDecoration(itemDecoration)
        }

        binding.openmojiPickerToolbar.let {
            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            it.menu.findItem(R.id.openmoji_picker_save_recent).also { saveRecentMenuItem ->
                saveRecentMenuItem.setOnMenuItemClickListener {
                    viewModel.toggleSaveRecent()
                    true
                }
                viewModel.saveRecent.observe(viewLifecycleOwner) { saveRecent ->
                    saveRecentMenuItem.isChecked = saveRecent
                }
            }

            it.menu.findItem(R.id.openmoji_picker_clear_recent).also { clearRecentMenuItem ->
                clearRecentMenuItem.setOnMenuItemClickListener {
                    viewModel.clearRecent()
                    true
                }
                viewModel.hasRecent.observe(viewLifecycleOwner) { hasRecent ->
                    clearRecentMenuItem.isVisible = hasRecent
                }
            }
        }

        binding.openmojiPickerProgressBar.let {
            spanSizeGridLayoutManagerViewModel.isLayoutFinished.observe(viewLifecycleOwner) { isLayoutFinished ->
                it.isVisible = !isLayoutFinished
            }
        }
    }

    override fun openMojiOnClick(binding: OpenmojiPickerItemOpenmojiBinding, openMoji: OpenMoji, position: Int) {
        viewModel.saveRecent(openMoji)
        requireActivity().setResult(
            Activity.RESULT_OK,
            Intent().putExtras(
                bundleOf(
                    KEY_OPENMOJI to openMoji
                ),
            ),
        )
        requireActivity().finish()
    }

    companion object {
        const val KEY_OPENMOJI = "openmoji"
    }
}
