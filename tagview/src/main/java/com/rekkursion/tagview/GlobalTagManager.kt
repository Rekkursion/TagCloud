package com.rekkursion.tagview

import android.util.Log
import kotlin.math.max

object GlobalTagManager {
    private val mExistingTagsAndCounts: HashMap<String, Int> = hashMapOf()

    fun doesTagExist(tagString: String): Boolean
            = mExistingTagsAndCounts.containsKey(tagString) && mExistingTagsAndCounts[tagString]!! > 0

    fun hadTagBeenExisted(tagString: String): Boolean
            = mExistingTagsAndCounts.containsKey(tagString)

    fun addTag(tagString: String) {
        mExistingTagsAndCounts[tagString] = (mExistingTagsAndCounts[tagString] ?: 0) + 1
    }

    fun removeTag(tagString: String) {
        if (hadTagBeenExisted(tagString))
            mExistingTagsAndCounts[tagString] = max(0, mExistingTagsAndCounts[tagString]!! - 1)
    }

    fun existTagList(): List<String> = mExistingTagsAndCounts.filter { it.value > 0 }.keys.toList()

    fun hadBeenExistedTagList(): List<String> = mExistingTagsAndCounts.keys.toList()
}