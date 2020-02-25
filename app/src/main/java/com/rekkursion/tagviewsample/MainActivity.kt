package com.rekkursion.tagviewsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            TagView.DefaultBackgroundColor.BLUE.color,
            TagView.DefaultBackgroundColor.YELLOW.color
        )

        // set the remove listener
        tagCloud.setOnTagRemoveListener(object: OnTagRemoveListener {
            override fun onTagRemove(tagCloud: TagCloud, tagView: TagView, index: Int, numOfTagsAfterRemoving: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "#$index removed, remains $numOfTagsAfterRemoving tag${if (numOfTagsAfterRemoving <= 1) "" else "s"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        // set the listener when the new tag's string has already been added
        tagCloud.setOnTagStringConflictListener(object: OnTagStringConflictListener {
            override fun onTagStringConflict(tagCloud: TagCloud, conflictedString: String) {
                Toast.makeText(this@MainActivity, "\"$conflictedString\" has already been added", Toast.LENGTH_SHORT).show()
            }
        })

        // set the click listener
        tagCloud.setOnTagClickListener(object: OnTagClickListener {
            override fun onTagClick(tagCloud: TagCloud, tagView: TagView, index: Int) {
                Toast.makeText(this@MainActivity, "#$index has been clicked", Toast.LENGTH_SHORT).show()
            }
        })

        // the edit-text for the new tag's string
        val edtText = findViewById<EditText>(R.id.edt_text)

        // add a new tag into the tag-cloud
        val btnAddTag = findViewById<Button>(R.id.btn_add_tag)
        btnAddTag.setOnClickListener {
            tagCloud.addTag(edtText.text.toString())
        }

        val btnSetIsIndicatorOrNot = findViewById<Button>(R.id.btn_set_is_indicator_or_not)
        btnSetIsIndicatorOrNot.setOnClickListener {
            tagCloud.isIndicator = tagCloud.isIndicator.not()
        }

        // show or hide the appearing times
        val btnShowOrHideAppearingTimes = findViewById<Button>(R.id.btn_show_or_hide_appearing_times)
        btnShowOrHideAppearingTimes.setOnClickListener {
            tagCloud.isShowingAppearingTimes = tagCloud.isShowingAppearingTimes.not()
        }
    }
}
