package com.example.note.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.MainActivity
import com.example.note.R
import com.example.note.adapter.NoteAdapter
import com.example.note.databinding.FragmentHomeBinding
import com.example.note.model.Note
import com.example.note.viewmodel.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider{
    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var viewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        binding.fabAdd.setOnClickListener(){
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    private fun updateUI(noteList: List<Note>){
        if(noteList!=null){
            if(noteList.isEmpty()){
                binding.mainRV.visibility = View.GONE
            }else{
                binding.mainRV.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpRecyclerView(){
        val staggeredGridLayout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        noteAdapter = NoteAdapter()

        binding.mainRV.apply {
            layoutManager = staggeredGridLayout
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {
            viewModel.getAllNotes().observe(viewLifecycleOwner){
                note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }

    }

    private fun searchNote(query: String?){
        val searchQuery = "%$query"

        viewModel.searchNote(searchQuery).observe(this){
            note ->
            noteAdapter.differ.submitList(note)
            updateUI(note)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchNote(newText)
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.search).actionView as SearchView
        search.isSubmitButtonEnabled = false
        search.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }
}