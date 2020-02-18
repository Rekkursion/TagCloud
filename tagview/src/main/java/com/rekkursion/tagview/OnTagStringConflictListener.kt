package com.rekkursion.tagview

interface OnTagStringConflictListener {
    fun onTagStringConflict(tagCloud: TagCloud, conflictedString: String)
}