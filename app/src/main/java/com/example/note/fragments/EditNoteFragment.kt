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
import android.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.note.MainActivity
import com.example.note.R
import com.example.note.databinding.FragmentEditNoteBinding
import com.example.note.model.Note
import com.example.note.viewmodel.NoteViewModel

class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {
    private var editBinding : FragmentEditNoteBinding? = null
    private val binding get() = editBinding!!

    private lateinit var noteViewModel : NoteViewModel
    private lateinit var currentNote : Note

    private val args : EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        noteViewModel = (activity as MainActivity).viewModel

        currentNote = args.note!!

        binding.editTextTitleEdit.setText(currentNote.noteTitle)
        binding.editTextContentEdit.setText(currentNote.noteContent)

        binding.fabSave.setOnClickListener(){
            val noteTitle = binding.editTextTitleEdit.text.toString().trim()
            val noteContent = binding.editTextContentEdit.text.toString().trim()

            if(noteTitle.isEmpty() || noteContent.isEmpty()){
                Toast.makeText(view.context, "Please enter all the fields.", Toast.LENGTH_SHORT).show()
            }
            else{
                val updatedNote = Note(currentNote.id, noteTitle, noteContent)
                noteViewModel.updateNote(updatedNote)
                view.findNavController().popBackStack(R.id.homeFragment, false)
                Toast.makeText(view.context, "Note Updated Successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteNote(){
        val alert = AlertDialog.Builder(activity)
        alert.apply {
            setTitle("Delete Note.")
            setMessage("Are you sure you want to delete this Note?")
            setPositiveButton("YES"){
                _,_ ->
                noteViewModel.deleteNote(currentNote)
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
                Toast.makeText(context, "Note Deleted Successfully!", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("NO", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.delete ->{
                deleteNote()
                true
            }
            else -> false
        }
    }
}