package com.rekkursion.tagview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import com.google.android.flexbox.FlexboxLayout

class TagCloud(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    // if this tag-cloud is an indicator or not, default: false
    private var mIsIndicator: Boolean = false
    var isIndicator
        get() = mIsIndicator
        set(value) {
            mIsIndicator = value
            if (mIsIndicator) {
                mFblTagsContainer.children
                    .forEach { (it as? TagView)?.setCloseImageButtonVisibility(View.GONE) }
            }
            else {
                mFblTagsContainer.children
                    .forEach { (it as? TagView)?.setCloseImageButtonVisibility(View.VISIBLE) }
            }
        }

    // should show the appearing times or not, default: true
    private var mIsShowingAppearingTimes: Boolean = true
    var isShowingAppearingTimes
        get() = mIsShowingAppearingTimes
        set(value) {
            mIsShowingAppearingTimes = value
            mFblTagsContainer.children
                .forEach { (it as? TagView)?.setShouldShowAppearingTimes(mIsShowingAppearingTimes) }
        }

    // for placing all tag-views
    private val mFblTagsContainer: FlexboxLayout

    // for storing the already-exists tag-views
    private val mTagStringsHashMap: HashMap<String, TagView> = hashMapOf()

    // the listener when a new tag's string has already added before
    private var mOnTagStringConflictListener: OnTagStringConflictListener? = null

    // the listener when a certain tag-view is removed
    private var mOnTagRemoveListener: OnTagRemoveListener? = null

    // the listener when a certain tag-view is clicked
    private var mOnTagClickListener: OnTagClickListener? = null

    // the possible background colors to be randomly chosen when creating a new tag-view
    private var mPossibleBackgroundColorsHashSet: HashSet<Int> = hashSetOf()
    var possibleBackgroundColors
        get() = HashSet(mPossibleBackgroundColorsHashSet)
        set(value) {
            mPossibleBackgroundColorsHashSet.clear()
            mPossibleBackgroundColorsHashSet.addAll(value)
        }

    /* =================================================================== */

    // primary constructor
    init {
        // inflate the layout
        LayoutInflater.from(context).inflate(R.layout.view_tag_cloud, this)

        // get views
        mFblTagsContainer = findViewById(R.id.fbl_tags_container)
    }

    /* =================================================================== */

    /** adders & removers */

    // add a new tag
    fun addTag(tagString: String, index: Int? = null) {
        // create the tag-view
        val tagView = if (mPossibleBackgroundColorsHashSet.isEmpty()) TagView(context, tagString, mIsIndicator, mIsShowingAppearingTimes) else TagView(context, tagString, mIsIndicator, mIsShowingAppearingTimes, mPossibleBackgroundColorsHashSet)
        // set the on-remove-listener of this tag-view
        tagView.setOnRemoveListener(object: TagView.OnRemoveListener {
            override fun onRemove() {
                for ((idx, it) in mFblTagsContainer.children.withIndex()) {
                    if (it == tagView) {
                        removeTagByIndex(idx)
                        break
                    }
                }
            }
        })

        // set the on-click-listener of this tag-view
        tagView.setOnClickListener { mOnTagClickListener?.onTagClick(
            this@TagCloud,
            tagView,
            mFblTagsContainer.children.indexOf(tagView)
        ) }

        // the string has already been added
        if (mTagStringsHashMap.containsKey(tagString))
            mOnTagStringConflictListener?.onTagStringConflict(this, tagString)
        // no problem
        else {
            // add the created tag-view into the container
            if (index == null) mFblTagsContainer.addView(tagView)
            else mFblTagsContainer.addView(tagView, index)

            // add the string of new tag into the hash-set
            mTagStringsHashMap[tagString] = tagView
        }
    }

    // remove a certain tag-view by index
    fun removeTagByIndex(index: Int): Boolean {
        if (index < 0 || index >= mFblTagsContainer.childCount)
            return false

        val tagView = (mFblTagsContainer.getChildAt(index) as? TagView) ?: return false
        mFblTagsContainer.removeView(tagView)
        mOnTagRemoveListener?.onTagRemove(this@TagCloud, tagView, index, mFblTagsContainer.childCount)
        mTagStringsHashMap.remove(tagView.tagString)

        return true
    }

    // remove a certain tag-view by string
    fun removeTagByString(tagString: String): Boolean {
        for ((idx, it) in mFblTagsContainer.children.withIndex())
            if (it is TagView && it.tagString == tagString)
                return removeTagByIndex(idx)
        return false
    }

    /* =================================================================== */

    /** getters */

    // get the tag-view by index
    fun getTagAt(index: Int): TagView = mFblTagsContainer.getChildAt(index) as TagView

    // get the tag string of a tag-view by index
    fun getTagStringAt(index: Int): String? = getTagAt(index).tagString

    /* =================================================================== */

    /** setters */

    // set the listener when a new tag's string has already added before
    fun setOnTagStringConflictListener(onTagStringConflictListener: OnTagStringConflictListener) {
        mOnTagStringConflictListener = onTagStringConflictListener
    }

    // set the listener when a certain tag-view is about to be removed
    fun setOnTagRemoveListener(onTagRemoveListener: OnTagRemoveListener) {
        mOnTagRemoveListener = onTagRemoveListener
    }

    // the listener when a certain tag-view is clicked
    fun setOnTagClickListener(onTagClickListener: OnTagClickListener) {
        mOnTagClickListener = onTagClickListener
    }
}