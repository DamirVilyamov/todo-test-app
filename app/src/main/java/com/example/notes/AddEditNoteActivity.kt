package com.example.notes

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
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
    private val TAG = "!@#"

    var title: String? = ""
    var description: String? = ""
    var imagesList: ArrayList<String>? = ArrayList()
    var createdDate: String? = ""
    var updatedDate: String? = ""
    var id:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        processIntent()
    }

    private fun processIntent() {
        MODE = intent.getIntExtra("MODE", 0)
        Log.d(TAG, "processIntent: MODE = $MODE")
        activateEditMode(false)
        when (MODE) {
            IntentCodes.ADD -> {

            }
            IntentCodes.INFO -> {
                title = intent.getStringExtra("TITLE")
                description = intent.getStringExtra("DESCRIPTION")
                createdDate = intent.getStringExtra("CREATED_DATE")
                updatedDate = intent.getStringExtra("UPDATED_DATE")
                imagesList = intent.getStringArrayListExtra("IMAGES")
                id = intent.getIntExtra("ID", -1)
                currentNote = Note(title!!, description!!, createdDate!!, updatedDate!!)
                currentNote.id = id!!
                printNoteInfo(currentNote)
            }
        }
    }

    private fun printNoteInfo(note: Note){
        edit_text_title.setText(note.title, TextView.BufferType.EDITABLE)
        edit_text_description.setText(note.description, TextView.BufferType.EDITABLE)
        text_view_created.text = "Created: " + note.createdDate
        text_view_updated.text = "Updated: " + note.updatedDate
    }



    private fun getCurrentDate(): String {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        return currentDate
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    private fun activateEditMode(isActive: Boolean) {
        isInEditMode = isActive
        if (isActive) {
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
                Log.d(TAG, "onOptionsItemSelected: MODE = $MODE")
                if (MODE == IntentCodes.ADD) {
                    Log.d(TAG, "onOptionsItemSelected: inserting new note ${currentNote.id}")
                    noteViewModel.insert(currentNote)
                } else if (MODE == IntentCodes.INFO) {
                    noteViewModel.update(currentNote)
                    Log.d(TAG, "onOptionsItemSelected: updating note ${currentNote.id} ")
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
        val note = Note(title!!, description!!,/* imagesList!!,*/ createdDate!!, updatedDate!!)
        Log.d(TAG, "getNote: $note")
        return note

    }

}