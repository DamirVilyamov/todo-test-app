package com.example.notes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*


class NotesAdapter : ListAdapter<Note, NotesAdapter.NoteHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> =
            object : DiffUtil.ItemCallback<Note>() {
                override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.getID() == newItem.getID()
                }

                override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.description == newItem.description &&
                            oldItem.updatedDate == newItem.updatedDate
                }
            }
    }

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currentNote: Note) {
            itemView.text_view_title.text = currentNote.title
            itemView.text_view_description.text = currentNote.description
            itemView.text_view_last_changed_date.text = currentNote.updatedDate
            itemView.setOnClickListener {
                val editNoteIntent = Intent(itemView.context, AddEditNoteActivity::class.java)
                editNoteIntent.putExtra("MODE", IntentCodes.INFO)
                editNoteIntent.putExtra("ID", currentNote.getID())
                editNoteIntent.putExtra("TITLE", currentNote.title)
                editNoteIntent.putExtra("DESCRIPTION", currentNote.title)
                editNoteIntent.putExtra("IMAGES", currentNote.imagesList)
                editNoteIntent.putExtra("CREATED_DATE", currentNote.createdDate)
                editNoteIntent.putExtra("UPDATED_DATE", currentNote.updatedDate)

                itemView.context.startActivity(editNoteIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = getItem(position)
        holder.bind(currentNote)
    }


}