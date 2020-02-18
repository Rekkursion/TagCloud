package com.rekkursion.tagview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.TextView
import kotlin.random.Random

class TagView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    enum class DefaultBackgroundColor(val color: Int) {
        RED(Color.parseColor("#d9534f")),
        GREEN(Color.parseColor("#5cb85c")),
        BLUE(Color.parseColor("#0275d8")),
        YELLOW(Color.parseColor("#f0ad4e")),
        GRAY(Color.LTGRAY);

        companion object {
            fun getColorsHashSet(): HashSet<Int> = values().map { it.color }.toHashSet()
        }
    }

    /* =================================================================== */

    // the root of this tag-view
    private val mHsvRoot: HorizontalScrollView

    // the text-view for showing the tag string
    private val mTxtvString: TextView
//    var tagString: String get() = mTxtvString.text.toString(); set(value) { mTxtvString.text = value }
    val tagString: String get() = mTxtvString.text.toString()

    // the text-view for showing the appearing times
    private val mTxtvAppearingTimes: TextView

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
        mHsvRoot = findViewById(R.id.hsv_root)
        mTxtvString = findViewById(R.id.txtv_string)
        mTxtvAppearingTimes = findViewById(R.id.txtv_appearing_times)
        mImgbtnClose = findViewById(R.id.imgbtn_close)

        // set events
        mImgbtnClose.setOnClickListener {
            mOnRemoveListener?.onRemove()
        }
    }

    // secondary constructor
    @SuppressLint("SetTextI18n")
    constructor(context: Context,
                tagString: String,
                isIndicator: Boolean,
                isShowingAppearingTimes: Boolean,
                possibleBackgroundColors: HashSet<Int> = DefaultBackgroundColor.getColorsHashSet()): this(context) {
        // set the tag string
        mTxtvString.text = tagString

        // set the visibility of imgbtn-close
        if (isIndicator)
            mImgbtnClose.visibility = View.GONE

        // set the visibility of txtv-appearing-times
        if (!isShowingAppearingTimes)
            mTxtvAppearingTimes.visibility = View.GONE

        // set the text of txtv-appearing-times
        updateAppearingTimes()

        // set the background color
        mHsvRoot.setBackgroundResource(R.drawable.background_tag_view)
        (mHsvRoot.background as GradientDrawable).setColor(possibleBackgroundColors.elementAt(Random.nextInt(possibleBackgroundColors.size)))
    }

    /* =================================================================== */

    internal fun setOnRemoveListener(onRemoveListener: OnRemoveListener) {
        mOnRemoveListener = onRemoveListener
    }

    internal fun setCloseImageButtonVisibility(visibility: Int) {
        mImgbtnClose.visibility = visibility
    }

    internal fun setShouldShowAppearingTimes(shouldShowAppearingTimes: Boolean) {
        mTxtvAppearingTimes.visibility = if (shouldShowAppearingTimes)
            View.VISIBLE
        else
            View.GONE
        updateAppearingTimes()
    }

    @SuppressLint("SetTextI18n")
    internal fun updateAppearingTimes() {
        mTxtvAppearingTimes.text = "(${GlobalTagManager.getTagAppearingTimes(mTxtvString.text.toString())})"
    }

    /* =================================================================== */

    override fun setOnClickListener(l: OnClickListener?) {
        mTxtvString.setOnClickListener(l)
    }

    /* =================================================================== */

    // interface: be invoked when removing this tag
    internal interface OnRemoveListener {
        fun onRemove()
    }
}