package dev.ebnbin.openmojipicker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

internal class OpenMojiPickerSpinnerAdapter(
    context: Context,
    objects: List<OpenMojiGroup>,
) : ArrayAdapter<OpenMojiGroup>(
    context,
    R.layout.openmoji_picker_spinner_item,
    android.R.id.text1,
    objects,
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = getItem(position)
        requireNotNull(item)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item.group
        view.findViewById<TextView>(R.id.openmoji_picker_count).text = "${item.openMojiCount}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val item = getItem(position)
        requireNotNull(item)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item.group
        view.findViewById<TextView>(R.id.openmoji_picker_count).text = "${item.openMojiCount}"
        return view
    }
}
