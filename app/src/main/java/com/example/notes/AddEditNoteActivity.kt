package com.example.notes

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_add_edit_note.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var currentNote: Note
    private var MODE: Int = -1
    private var isInEditMode = false

    var title: String? = ""
    var description: String? = ""
    var imagesList:ArrayList<String>? = ArrayList()
    var createdDate:String? = ""
    var updatedDate:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        processIntent()
    }

    private fun processIntent() {
        MODE = intent.getIntExtra("MODE", 0)
        when (MODE) {
            IntentCodes.ADD -> {

            }
            IntentCodes.INFO -> {
                title = intent.getStringExtra("TITLE")
                description = intent.getStringExtra("DESCRIPTION")
                createdDate = intent.getStringExtra("CREATED_DATE")
                updatedDate = intent.getStringExtra("UPDATED_DATE")
                imagesList = intent.getStringArrayListExtra("IMAGES")
            }
        }
    }

    private fun getCurrentDate(): String {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        return currentDate
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    private fun activateEditMode(active: Boolean) {
        isInEditMode = active
        if (active) {
            edit_text_description.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            edit_text_title.inputType = InputType.TYPE_CLASS_TEXT
        } else {
            edit_text_description.inputType = InputType.TYPE_NULL
            edit_text_title.inputType = InputType.TYPE_NULL
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_note -> {
                activateEditMode(true)
                return true
            }
            R.id.save_note -> {
                currentNote = getNote()
                if (MODE == IntentCodes.ADD) {
                    noteViewModel.insert(currentNote)
                } else if (MODE == IntentCodes.INFO) {
                    noteViewModel.update(currentNote)
                }
                activateEditMode(false)
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getNote(): Note {
        title = edit_text_title.text.toString()
        description = edit_text_description.text.toString()
        if (text_view_created.text == getString(R.string.created_date_default)) {
            createdDate = getCurrentDate()
            updatedDate = getCurrentDate()
        } else {
            updatedDate = getCurrentDate()
        }

        return Note(title!!, description!!, imagesList!!, createdDate!!, updatedDate!!)

    }

}