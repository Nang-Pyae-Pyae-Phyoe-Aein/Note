package com.example.note.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.note.MainActivity
import com.example.note.R
import com.example.note.databinding.FragmentAddNoteBinding
import com.example.note.model.Note
import com.example.note.viewmodel.NoteViewModel

class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    private var addBinding : FragmentAddNoteBinding? = null
    private val binding get() = addBinding!!

    private lateinit var noteViewMode : NoteViewModel
    private lateinit var addNoteView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addBinding = FragmentAddNoteBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        noteViewMode = (activity as MainActivity).viewModel

        addNoteView = view
    }

    private fun saveNote(view : View){
        val noteTitle = binding.editTextTitle.text.toString().trim()
        val noteContent = binding.editTextContent.text.toString().trim()

        if(noteTitle.isEmpty() || noteContent.isEmpty()){
            Toast.makeText(view.context, "Please enter all the field(s).", Toast.LENGTH_SHORT).show()
        }
        else{
            val note = Note(0, noteTitle, noteContent)
            noteViewMode.addNote(note)

            Toast.makeText(view.context, "Noted Added Successfully!", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.save_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.save -> {
                saveNote(addNoteView)
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addBinding = null
    }
}