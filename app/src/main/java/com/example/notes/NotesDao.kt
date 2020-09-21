package com.example.notes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note?)

    @Update
    fun update(note: Note?)

    @Delete
    fun delete(note: Note?)

    @Query("DELETE FROM notes_table")
    fun deleteAllNotes()

    @get:Query("SELECT * FROM notes_table ORDER BY updatedDate DESC")
    val allNotes: LiveData<List<Note?>?>?
}
