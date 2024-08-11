package com.example.note.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.databinding.ListNotesBinding
import com.example.note.fragments.HomeFragmentDirections
import com.example.note.model.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){
    inner class NoteViewHolder(val binding : ListNotesBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.noteTitle == newItem.noteTitle &&
                    oldItem.noteContent == newItem.noteContent
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(ListNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.binding.textViewTitle.text = currentNote.noteTitle
        holder.binding.textViewTitle.isSelected = true
        holder.binding.textViewContent.text = currentNote.noteContent

        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.translate_anim)
        holder.itemView.animation = animation

        holder.itemView.setOnClickListener(){
            val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }
}