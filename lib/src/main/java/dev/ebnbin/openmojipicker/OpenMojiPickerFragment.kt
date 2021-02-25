package dev.ebnbin.openmojipicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dev.ebnbin.openmojipicker.databinding.OpenmojiPickerFragmentBinding

class OpenMojiPickerFragment : Fragment() {
    private val viewModel: OpenMojiPickerViewModel by viewModels()

    private lateinit var binding: OpenmojiPickerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = OpenmojiPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var layoutManager: GridLayoutManager

    private lateinit var adapter: OpenMojiPickerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = GridLayoutManager(requireContext(), 5)
        adapter = OpenMojiPickerAdapter()
        adapter.submitList(viewModel.openMojiPickerItemList)

        binding.openmojiPickerRecyclerView.let {
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }
}
