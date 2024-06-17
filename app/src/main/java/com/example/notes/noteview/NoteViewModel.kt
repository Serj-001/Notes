package com.example.notes.noteview

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notes.data.Note
import com.example.notes.data.NoteRoomDatabase

class NoteViewModel(application: Application) : ViewModel() {

    val noteList: LiveData<List<Note>>
    private val repository: NoteRepository
    var noteTitleVal by mutableStateOf("")
    var noteDescriptionVal by mutableStateOf("")

    init {
        val noteDb = NoteRoomDatabase.getInstance(application)
        val noteDao = noteDb.noteDao()
        repository = NoteRepository(noteDao)
        noteList = repository.noteList
    }

    fun changeTitle(value: String) {
        noteTitleVal = value
    }
    fun changeDescription(value: String) {
        noteDescriptionVal = value
    }
    fun addNote() {
        repository.addNote(Note(noteTitleVal, noteDescriptionVal))
    }
    fun deleteNote(id: Int) {
        repository.deleteNote(id)
    }
}