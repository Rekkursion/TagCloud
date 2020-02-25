package com.rekkursion.tagview

import kotlin.math.max

object GlobalTagManager {
    private val mExistingTagsAndCounts: HashMap<String, Int> = hashMapOf()

    // check if a certain tag exists currently
    fun doesTagExist(tagString: String): Boolean
            = mExistingTagsAndCounts.containsKey(tagString) && mExistingTagsAndCounts[tagString]!! > 0

    // check if a certain tag had been existed in the past time (but has been removed now)
    fun hadTagBeenExisted(tagString: String): Boolean
            = mExistingTagsAndCounts.containsKey(tagString)

    // get the appearing times of a certain tag
    fun getTagAppearingTimes(tagString: String): Int
            = mExistingTagsAndCounts.getOrDefault(tagString, 0)

    // add a tag
    fun addTag(tagString: String) {
        mExistingTagsAndCounts[tagString] = (mExistingTagsAndCounts[tagString] ?: 0) + 1
    }

    // remove a tag
    fun removeTag(tagString: String) {
        if (hadTagBeenExisted(tagString))
            mExistingTagsAndCounts[tagString] = max(0, mExistingTagsAndCounts[tagString]!! - 1)
    }

    // return the list of currently-exist tags
    fun existTagList(): List<String> = mExistingTagsAndCounts.filter { it.value > 0 }.keys.toList()

    // return the list of had-been-existed tags (tags that had been added but have been removed now)
    fun hadBeenExistedTagList(): List<String> = mExistingTagsAndCounts.keys.toList()
}