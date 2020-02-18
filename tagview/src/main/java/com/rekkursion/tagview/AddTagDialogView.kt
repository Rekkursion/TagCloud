package com.rekkursion.tagview

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import kotlinx.android.synthetic.main.view_add_tag_dialog.view.*

internal class AddTagDialogView(context: Context, attrs: AttributeSet? = null): LinearLayoutCompat(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_add_tag_dialog, this)

        btnSelect.setOnClickListener {
            if (GlobalTagManager.existTagList().isNotEmpty()) {
                AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.str_select_a_tag))
                    .setItems(GlobalTagManager.existTagList().toTypedArray()) { _, index ->
                        btnSelect.text = GlobalTagManager.existTagList().getOrNull(index) ?: context.getString(R.string.str_select_a_tag)
                    }
                    .create()
                    .show()
            } else {
                AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.str_there_is_no_tag))
                    .create()
                    .show()
            }
        }

        edtAddNew.visibility = View.GONE

        btnBackToSelect.visibility = View.GONE

        btnBackToSelect.setOnClickListener {
            btnSelect.visibility = View.VISIBLE
            edtAddNew.visibility = View.GONE
            btnBackToSelect.visibility = View.GONE
            btnOrAddNew.visibility = View.VISIBLE
        }

        btnOrAddNew.text = context.getString(R.string.str_or_create_a_new_tag)
        btnOrAddNew.setOnClickListener {
            btnSelect.visibility = View.GONE
            edtAddNew.visibility = View.VISIBLE
            btnBackToSelect.visibility = View.VISIBLE
            btnOrAddNew.visibility = View.GONE
        }
    }

    internal fun getTagString(): String = if (btnSelect.visibility == View.VISIBLE && GlobalTagManager.existTagList().isNotEmpty() && btnSelect.text.toString() != context.getString(R.string.str_select_a_tag))
        btnSelect.text.toString()
    else
        edtAddNew.text.toString()
}