package com.example.notes

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NotesRepository(
    application: Context,
    private var noteDao: NoteDao? = null,
    private var allnotes: LiveData<List<Note?>?>? = null,
    private var noteDatabase: NoteDatabase? = null
) {

    init {
        noteDatabase = NoteDatabase.getInstance(application)
        noteDao = noteDatabase!!.noteDao()
        allnotes = noteDao!!.allNotes
    }

    fun insert(note: Note?) {
        InsertNoteAsyncTask(noteDao!!).execute(note)
    }

    fun update(note: Note?) {
        UpdateNoteAsyncTask(noteDao!!).execute(note)
    }

    fun delete(note: Note?) {
        DeleteNoteAsyncTask(noteDao!!).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(noteDao!!).execute()
    }

    fun getAllNotes(): LiveData<List<Note?>?>? {
        return allnotes
    }

    class InsertNoteAsyncTask(private val noteDao: NoteDao?) : AsyncTask<Note, Unit, Unit>() {
        override fun doInBackground(vararg params: Note?) {
            noteDao!!.insert(params[0])
        }
    }

    class UpdateNoteAsyncTask(private val noteDao: NoteDao?) : AsyncTask<Note, Unit, Unit>() {
        override fun doInBackground(vararg params: Note?) {
            noteDao!!.update(params[0])
        }
    }

    class DeleteNoteAsyncTask(private val noteDao: NoteDao?) : AsyncTask<Note, Unit, Unit>() {
        override fun doInBackground(vararg params: Note?) {
            noteDao!!.delete(params[0])
        }

    }

    class DeleteAllNotesAsyncTask(private val noteDao: NoteDao?) : AsyncTask<Note, Unit, Unit>() {
        override fun doInBackground(vararg params: Note?) {
            noteDao!!.deleteAllNotes()
        }

    }

}