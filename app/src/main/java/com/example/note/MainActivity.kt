package com.example.note

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.note.database.NoteDatabase
import com.example.note.repository.NoteRepository
import com.example.note.viewmodel.NoteViewModel
import com.example.note.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()
    }
    private fun setUpViewModel(){
        val noteRepo = NoteRepository(NoteDatabase(this))
        val viewModelFactory = NoteViewModelFactory(application, noteRepo)
        viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
    }
}