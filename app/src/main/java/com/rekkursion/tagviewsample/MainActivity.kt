package com.rekkursion.tagviewsample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.rekkursion.tagview.OnTagRemoveListener
import com.rekkursion.tagview.OnTagStringConflictListener
import com.rekkursion.tagview.TagCloud
import com.rekkursion.tagview.TagView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the tag-cloud
        val tagCloud = findViewById<TagCloud>(R.id.tag_cloud)

        // set the possible background colors of all tag-views
        tagCloud.possibleBackgroundColors = hashSetOf(
            TagView.DefaultBackgroundColor.BLUE.color,
            TagView.DefaultBackgroundColor.YELLOW.color
        )

        // set the remove listener
        tagCloud.setOnTagRemoveListener(object: OnTagRemoveListener {
            override fun onTagRemove(tagCloud: TagCloud, tagView: TagView, index: Int, numOfTagsAfterRemoving: Int) {
                Log.e("onTagRemove", "[$index] -> $numOfTagsAfterRemoving")
            }
        })

        // set the listener when the new tag's string has already been added
        tagCloud.setOnTagStringConflictListener(object: OnTagStringConflictListener {
            override fun onTagStringConflict(tagCloud: TagCloud, conflictedString: String) {
                Log.e("onTagStringConflict", conflictedString)
            }
        })

        // the edit-text for the new tag's string
        val edtText = findViewById<EditText>(R.id.edt_text)

        // add a new tag into the tag-cloud
        val btnAddTag = findViewById<Button>(R.id.btn_add_tag)
        btnAddTag.setOnClickListener {
            tagCloud.addTag(edtText.text.toString())
        }
    }
}
