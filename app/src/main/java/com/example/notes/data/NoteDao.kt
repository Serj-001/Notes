package com.example.notes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getNotes() : LiveData<List<Note>>

    @Insert
    fun addNote(note: Note)

    @Query("DELETE FROM notes WHERE noteId =:id")
    fun deleteNote(id: Int)
}