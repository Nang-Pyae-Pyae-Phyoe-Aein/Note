package com.example.note.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


abstract class NoteDatabase : RoomDatabase(){
    //which declare the instance of NoteDao interface
    abstract fun getNoteDao() : NoteDao

    companion object{
        @Volatile
        private var instance : NoteDatabase? = null
        private val LOCK = Any()

        operator fun  invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "notes_db"
            ).build()
    }
}