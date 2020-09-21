package com.example.notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object {
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"
                )
                    .fallbackToDestructiveMigration()
                    //.addCallback(NoteDatabase.roomCallback)
                    .build()
            }
            return instance!!
        }
    }

    private val roomCallback: Callback = object : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //PopulateDbAsyncTask(instance).execute()
        }
    }

    /* private class PopulateDbAsyncTask private constructor(db: NoteDatabase?) :
         AsyncTask<Void?, Void?, Void?>() {
         private val noteDao: NoteDao?
         protected override fun doInBackground(vararg voids: Void): Void? {
             noteDao!!.insert(
                 Note(
                     "This is your Title 1",
                     "Here you can write some descriptive information",
                     10
                 )
             )
             noteDao!!.insert(
                 Note(
                     "This is your Title 2",
                     "Here you can write some descriptive information",
                     7
                 )
             )
             noteDao!!.insert(
                 Note(
                     "This is your Title 3",
                     "Here you can write some descriptive information",
                     9
                 )
             )
             return null
         }

         init {
             noteDao = db!!.noteDao()
         }
     }*/
}