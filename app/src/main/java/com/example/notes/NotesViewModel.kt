package com.example.notes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "!@#"
    private val repository: NotesRepository = NotesRepository(application)
    private val allNotes: LiveData<List<Note?>?>?

    fun insert(note: Note?) {
        Log.d(TAG, "viewModel insert: $note")
        repository.insert(note)
    }

    fun update(note: Note?) {
        Log.d(TAG, "viewModel update: $note")
        repository.update(note)
    }

    fun delete(note: Note?) {
        Log.d(TAG, "viewModel delete: $note")
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Note?>?>? {
        return allNotes
    }

    init {
        allNotes = repository.getAllNotes()
    }
}