package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_add_edit_note.*
import java.text.SimpleDateFormat
import java.util.*


class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var currentNote: Note
    private var MODE: Int = -1 //needed to edit or just show the info
    private var isInEditMode = false //needed to edit or just show the info
    private val TAG = "!@#"

    var title: String? = ""
    var description: String? = ""
    var createdDate: String? = ""
    var updatedDate: String? = ""
    var id: Int? = null
    var uri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        activateEditMode(false)
        processIntent()

        button_pick_image.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                IntentCodes.SELECT_PICTURE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentCodes.SELECT_PICTURE) {
                uri = data?.data.toString()
                note_image.setImageURI(uri?.toUri())
            }
        }
    }

    private fun processIntent() {//getting all the info about the note
        MODE = intent.getIntExtra("MODE", 0)
        Log.d(TAG, "processIntent: MODE = $MODE")

        when (MODE) {
            IntentCodes.ADD -> {

            }
            IntentCodes.INFO -> {
                title = intent.getStringExtra("TITLE")
                description = intent.getStringExtra("DESCRIPTION")
                createdDate = intent.getStringExtra("CREATED_DATE")
                updatedDate = intent.getStringExtra("UPDATED_DATE")
                uri = intent.getStringExtra("IMAGE")
                id = intent.getIntExtra("ID", -1)
                currentNote = Note(title!!, description!!, createdDate!!, updatedDate!!, uri)
                currentNote.id = id
                Log.d(TAG, "processIntent: got from intents: id $currentNote.id $currentNote")
                displayNoteInfo(currentNote)
            }
        }
    }

    private fun displayNoteInfo(note: Note) {
        edit_text_title.setText(note.title, TextView.BufferType.EDITABLE)
        edit_text_description.setText(note.description, TextView.BufferType.EDITABLE)
        text_view_created.text = "Created: " + note.createdDate
        text_view_updated.text = "Updated: " + note.updatedDate
        Log.d(TAG, "displayNoteInfo: showing image: ${note.imageUri}")
        note_image.setImageURI(note.imageUri?.toUri())
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
            button_pick_image.isEnabled = isActive
        } else {
            edit_text_description.inputType = InputType.TYPE_NULL
            edit_text_title.inputType = InputType.TYPE_NULL
            button_pick_image.isEnabled = isActive
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
        val note = Note(title!!, description!!, createdDate!!, updatedDate!!, uri)
        note.id = id
        Log.d(TAG, "getNote: $note.id $note")
        return note
    }

}