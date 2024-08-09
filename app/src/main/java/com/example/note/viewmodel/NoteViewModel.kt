package com.example.note.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.model.Note
import com.example.note.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(app : Application, private val repo : NoteRepository) : AndroidViewModel(app) {
    fun insertNote(note : Note) =
        viewModelScope.launch {
            repo.insertNote(note)
        }

    fun updateNote(note : Note) =
        viewModelScope.launch {
            repo.updateNote(note)
        }

    fun deleteNote(note : Note) =
        viewModelScope.launch {
            repo.deleteNote(note)
        }

    fun getAllNotes() = repo.getAllNotes()

    fun searchNote(query : String?) = repo.searchNote(query)
}