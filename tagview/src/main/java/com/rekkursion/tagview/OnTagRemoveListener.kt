package com.rekkursion.tagview

interface OnTagRemoveListener {
    fun onTagRemove(tagCloud: TagCloud, tagView: TagView, index: Int, numOfTagsAfterRemoving: Int)
}