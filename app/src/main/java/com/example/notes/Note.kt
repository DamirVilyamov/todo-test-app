package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "notes_table")
data class Note(
    var title: String,
    var description: String,
    val createdDate: String,
    var updatedDate: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = -1

    fun getID(): Int {
        return id
    }
}