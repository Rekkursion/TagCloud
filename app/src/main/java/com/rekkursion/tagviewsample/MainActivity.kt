package com.rekkursion.tagviewsample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rekkursion.tagview.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get the tag-cloud
        val tagCloud = findViewById<TagCloud>(R.id.tag_cloud)

        // set the possible background colors of all tag-views
        tagCloud.possibleBackgroundColors = hashSetOf(
            // the default background-colors
            TagView.DefaultBackgroundColor.BLUE.color,
            TagView.DefaultBackgroundColor.YELLOW.color,

            // user-defined background-colors
            Color.GRAY,
            Color.rgb(122, 59, 104)
        )

        // set the listener which is invoked when a tag is removed from this tag-cloud
        tagCloud.setOnTagRemoveListener(object: OnTagRemoveListener {
            override fun onTagRemove(tagCloud: TagCloud, tagView: TagView, index: Int, numOfTagsAfterRemoving: Int) {
                Toast.makeText(this@MainActivity, "#$index removed, remains $numOfTagsAfterRemoving tags", Toast.LENGTH_SHORT).show()
            }
        })

        // set the listener which is invoked when the new tag's string has already been added in this tag-cloud
        tagCloud.setOnTagStringConflictListener(object: OnTagStringConflictListener {
            override fun onTagStringConflict(tagCloud: TagCloud, conflictedString: String) {
                Toast.makeText(this@MainActivity, "\"$conflictedString\" has already been added", Toast.LENGTH_SHORT).show()
            }
        })

        // set the click listener on tags
        tagCloud.setOnTagClickListener(object: OnTagClickListener {
            override fun onTagClick(tagCloud: TagCloud, tagView: TagView, index: Int) {
                Toast.makeText(this@MainActivity, "#$index has been clicked", Toast.LENGTH_SHORT).show()
            }
        })

        // the edit-text for inputting the new tag's string
        val edtText = findViewById<EditText>(R.id.edt_text)

        // add a new tag into the tag-cloud
        val btnAddTag = findViewById<Button>(R.id.btn_add_tag)
        btnAddTag.setOnClickListener {
            tagCloud.addTag(edtText.text.toString())
        }

        // the button for setting the tag-cloud above is indicator or not
        val btnSetIsIndicatorOrNot = findViewById<Button>(R.id.btn_set_is_indicator_or_not)
        btnSetIsIndicatorOrNot.setOnClickListener {
            tagCloud.isIndicator = tagCloud.isIndicator.not()
        }
    }
}
