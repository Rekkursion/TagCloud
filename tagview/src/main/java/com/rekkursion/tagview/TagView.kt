package com.rekkursion.tagview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView

class TagView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    // the text-view for showing the tag string
    private val mTxtvString: TextView
    var tagString: String get() = mTxtvString.text.toString(); set(value) { mTxtvString.text = value }

    // the image-button for closing (removing) this tag-view
    private val mImgbtnClose: ImageButton

    // the listener when this tag-view is going to be removed
    private var mOnRemoveListener: OnRemoveListener? = null

    /* =================================================================== */

    // primary constructor
    init {
        // inflate the layout
        LayoutInflater.from(context).inflate(R.layout.view_tag, this)

        // get views
        mTxtvString = findViewById(R.id.txtv_string)
        mImgbtnClose = findViewById(R.id.imgbtn_close)

        // set events
        mImgbtnClose.setOnClickListener {
            mOnRemoveListener?.onRemove()
        }
    }

    // secondary constructor
    constructor(context: Context, tagString: String): this(context) {
        mTxtvString.text = tagString
    }

    /* =================================================================== */

    internal fun setOnRemoveListener(onRemoveListener: OnRemoveListener) {
        mOnRemoveListener = onRemoveListener
    }

    /* =================================================================== */

    internal interface OnRemoveListener {
        fun onRemove()
    }
}