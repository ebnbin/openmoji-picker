package dev.ebnbin.openmojipicker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import dev.ebnbin.eb.notNull

internal class OpenMojiPickerSpinnerAdapter(
    context: Context,
    objects: List<OpenMojiPickerItem>,
) : ArrayAdapter<OpenMojiPickerItem>(
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
        textView.text = item.group.notNull().openMojiGroup.group
        view.findViewById<TextView>(R.id.openmoji_picker_count).text = "${item.group.notNull().openMojiGroup.openMojiCount}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val item = getItem(position)
        requireNotNull(item)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item.group.notNull().openMojiGroup.group
        view.findViewById<TextView>(R.id.openmoji_picker_count).text = "${item.group.notNull().openMojiGroup.openMojiCount}"
        return view
    }
}
