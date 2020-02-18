package com.rekkursion.tagview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import kotlinx.android.synthetic.main.view_add_tag_dialog.view.*

internal class AddTagDialogView(context: Context, attrs: AttributeSet? = null): LinearLayoutCompat(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_add_tag_dialog, this)

        spnSelect.adapter = ArrayAdapter<String>(
            context,
            R.layout.spinner_item_add_tag_dialog,
            GlobalTagManager.existTagList()
        )

        edtAddNew.visibility = View.GONE

        btnBackToSelect.visibility = View.GONE

        btnBackToSelect.setOnClickListener {
            spnSelect.visibility = View.VISIBLE
            edtAddNew.visibility = View.GONE
            btnBackToSelect.visibility = View.GONE
            btnOrAddNew.visibility = View.VISIBLE
        }

        btnOrAddNew.text = context.getString(R.string.str_or_create_a_new_tag)
        btnOrAddNew.setOnClickListener {
            spnSelect.visibility = View.GONE
            edtAddNew.visibility = View.VISIBLE
            btnBackToSelect.visibility = View.VISIBLE
            btnOrAddNew.visibility = View.GONE
        }
    }

    internal fun getTagString(): String = if (spnSelect.visibility == View.VISIBLE && GlobalTagManager.existTagList().isNotEmpty())
        spnSelect.selectedItem as String
    else
        edtAddNew.text.toString()
}