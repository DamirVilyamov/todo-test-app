package com.example.notes

import android.content.Intent
import android.view.*
import android.widget.TextView
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*


class NotesAdapter(var onNoteListener: OnNoteListener) :
    ListAdapter<Note, NotesAdapter.NoteHolder>(DIFF_CALLBACK) {

    companion object {
        var checkboxesVisible = false
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> =
            object : DiffUtil.ItemCallback<Note>() {
                override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.description == newItem.description &&
                            oldItem.updatedDate == newItem.updatedDate &&
                            oldItem.isChecked == newItem.isChecked

                }
            }
    }

    class NoteHolder(itemView: View, var onNoteListener: OnNoteListener) :
        RecyclerView.ViewHolder(itemView), View.OnLongClickListener, View.OnClickListener {

        var isChecked = false
        fun bind(currentNote: Note) {
            itemView.text_view_title.text = currentNote.title
            itemView.text_view_description.text = currentNote.description
            itemView.text_view_last_changed_date.text = currentNote.updatedDate

            itemView.checkbox.isChecked = isChecked||currentNote.isChecked

            if (checkboxesVisible) {
                itemView.checkbox.visibility = View.VISIBLE
            } else {
                itemView.checkbox.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            onNoteListener.onLongClick(adapterPosition)
            checkboxesVisible = true
            isChecked = true
            return true
        }

        override fun onClick(v: View?) {
            onNoteListener.onClick(adapterPosition)
        }

    }

    fun checkAll(areChecked: Boolean) {
        if (areChecked) {
            currentList.forEach {
                it.isChecked = true
            }
        } else {
            currentList.forEach {
                it.isChecked = false
            }
            checkboxesVisible = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteHolder(itemView, onNoteListener)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = getItem(position)
        holder.bind(currentNote)
    }

    interface OnNoteListener {
        fun onLongClick(position: Int)
        fun onClick(position: Int)
    }
}