package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName = "notes_table")
data class Note(
    var title: String,
    var description: String,
    var imagesList: ArrayList<String>,
    val createdDate: String,
    var updatedDate: String
){
    @PrimaryKey(autoGenerate = true)
    private var id:Int = -1
    fun getID(): Int {
        return id
    }
}