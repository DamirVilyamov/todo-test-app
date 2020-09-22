package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    var title: String,
    var description: String,
    val createdDate: String,
    var updatedDate: String,
    var imageUri: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id:Int? = 0

    var isChecked = false
}