package com.rekkursion.tagview

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.core.view.children
import com.google.android.flexbox.FlexboxLayout

class TagCloud(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    // for placing all tag-views
    private val mFblTagsContainer: FlexboxLayout

    // for adding new tag-view
    private val mImgbtnAddNewTag: ImageButton

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
        mImgbtnAddNewTag = findViewById(R.id.imgbtn_add_new_tag)

        // set events
        mImgbtnAddNewTag.setOnClickListener {
            // the edit-text for the user enters the tag string
            val edtNewTagString = EditText(context)
            edtNewTagString.requestFocus()

            // the dialog shows for typing
            val dialog = AlertDialog.Builder(context)
                .setTitle("Enter the tag")
                .setView(edtNewTagString)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create()
            dialog.setOnShowListener {
                val btnPositive = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                btnPositive.setOnClickListener {
                    if (doesStringAlreadyExists(edtNewTagString.text.toString())) {
                        AlertDialog.Builder(context)
                            .setMessage("The tag already exists")
                            .setPositiveButton("OK", null)
                            .create()
                            .show()
                    }
                    else {
                        addTag(edtNewTagString.text.toString())
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }

    /* =================================================================== */

    /** adders & removers */

    // add a new tag
    fun addTag(tagString: String, index: Int? = null) {
        // create the tag-view
        val tagView = if (mPossibleBackgroundColorsHashSet.isEmpty()) TagView(context, tagString) else TagView(context, tagString, mPossibleBackgroundColorsHashSet)
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
            if (index == null) mFblTagsContainer.addView(tagView, mFblTagsContainer.childCount - 1)
            else mFblTagsContainer.addView(tagView, if (index >= mFblTagsContainer.childCount) mFblTagsContainer.childCount - 1 else index)

            // add the string of new tag into the hash-set
            mTagStringsHashMap[tagString] = tagView
        }
    }

    // remove a certain tag-view by index
    fun removeTagByIndex(index: Int): Boolean {
        if (index < 0 || index >= mFblTagsContainer.childCount - 1)
            return false

        val tagView = (mFblTagsContainer.getChildAt(index) as? TagView) ?: return false
        mFblTagsContainer.removeView(tagView)
        mOnTagRemoveListener?.onTagRemove(this@TagCloud, tagView, index, mFblTagsContainer.childCount - 1)
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

    // get the number of tags
    fun getNumOfTags(): Int = mFblTagsContainer.childCount - 1

    // get all tag-views
    fun getAllTags(): List<TagView> = mFblTagsContainer.children.filter { it is TagView }.map { it as TagView }.toList()

    // get tag strings of all tag-views
    fun getAllTagStrings(): List<String> = mFblTagsContainer.children.filter { it is TagView }.map { (it as TagView).tagString }.toList()

    // get the tag-view by index
    fun getTagAt(index: Int): TagView? = mFblTagsContainer.getChildAt(index) as? TagView

    // get the tag string of a tag-view by index
    fun getTagStringAt(index: Int): String? = getTagAt(index)?.tagString

    // check if a string is conflicted w/ the already-exists tags
    fun doesStringAlreadyExists(str: String): Boolean = mTagStringsHashMap.containsKey(str)

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