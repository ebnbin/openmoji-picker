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
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerAdapter
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerItem
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerLayoutManager
import dev.ebnbin.openmojipicker.internal.OpenMojiPickerPrefs
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

    private lateinit var layoutManager: OpenMojiPickerLayoutManager

    private lateinit var adapter: OpenMojiPickerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = OpenMojiPickerLayoutManager(requireContext(), spanSizeGridLayoutManagerViewModel)
        adapter = OpenMojiPickerAdapter(this)
        spanSizeGridLayoutManagerViewModel.isLayoutFinished.observe(viewLifecycleOwner) {
            binding.openmojiPickerProgressBar.isVisible = !it
        }

        viewModel.itemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.openmojiPickerToolbar.let {
            val enableRecentMenuItem = it.menu.findItem(R.id.openmoji_picker_save_recent)
            enableRecentMenuItem.setOnMenuItemClickListener {
                OpenMojiPickerPrefs.saveRecent.value = !OpenMojiPickerPrefs.saveRecent.value
                true
            }
            OpenMojiPickerPrefs.saveRecent.observe(viewLifecycleOwner) { enableRecent ->
                enableRecentMenuItem.isChecked = enableRecent
            }
            val clearRecentMenuItem = it.menu.findItem(R.id.openmoji_picker_clear_recent)
            viewModel.hasRecent.observe(viewLifecycleOwner) { hasRecent ->
                clearRecentMenuItem.isVisible = hasRecent
            }
            clearRecentMenuItem.setOnMenuItemClickListener {
                viewModel.clearRecent()
                true
            }
            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        binding.openmojiPickerRecyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addItemDecoration(
                HeaderItemDecoration(it) { itemPosition ->
                    viewModel.itemList.value.notNull()[itemPosition].viewType == OpenMojiPickerItem.ViewType.OPENMOJI_GROUP
                },
            )
        }
    }

    override fun openMojiOnClick(binding: OpenmojiPickerItemOpenmojiBinding, openMoji: OpenMoji, position: Int) {
        if (OpenMojiPickerPrefs.saveRecent.value) {
            viewModel.saveRecent(openMoji)
        }
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
