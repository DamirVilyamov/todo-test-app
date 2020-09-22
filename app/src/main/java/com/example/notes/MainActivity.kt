package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NotesAdapter.OnNoteListener {
    private lateinit var noteViewModel: NoteViewModel
    val adapter = NotesAdapter(this)
    lateinit var menuItemCheckAll: MenuItem
    lateinit var menuItemUnCheckAll: MenuItem
    lateinit var menuItemDeleteChecked: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        initRecyclerView()

        button_add_note.setOnClickListener {
            val addNoteIntent = Intent(this, AddEditNoteActivity::class.java)
            addNoteIntent.putExtra("MODE", IntentCodes.ADD)
            startActivity(addNoteIntent)
        }

    }

    private fun initRecyclerView() {
        recycler_view_notes.layoutManager = LinearLayoutManager(this)
        recycler_view_notes.adapter = adapter
        noteViewModel.getAllNotes()?.observe(this, { notes -> //update recyclerview here
            adapter.submitList(notes)
        })
    }


    private fun setObserverForList() {
        noteViewModel.getAllNotes()?.observe(this, { notes ->
            if (notes?.any {
                    it!!.isChecked
                }!!) {
                menuItemCheckAll.isVisible = true
            } else if (!notes.any { it!!.isChecked }) {
                menuItemUnCheckAll.isVisible = false
                menuItemCheckAll.isVisible = false
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuItemCheckAll = menu!!.findItem(R.id.check_all)
        menuItemUnCheckAll = menu.findItem(R.id.uncheck_all)
        menuItemDeleteChecked = menu.findItem(R.id.delete_selected)
        setObserverForList()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes' been deleted", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.delete_selected -> {

                noteViewModel.getAllNotes()?.observe(this, { notes -> //update recyclerview here
                    notes?.forEach {
                        if (it!!.isChecked) {
                            noteViewModel.delete(it)
                        }
                    }
                })
                NotesAdapter.checkboxesVisible = false
                item.isVisible = false
                adapter.notifyDataSetChanged()
            }
            R.id.check_all -> {
                adapter.checkAll(true)
                adapter.notifyDataSetChanged()
                item.isVisible = false
                menuItemUnCheckAll.isVisible = true
            }
            R.id.uncheck_all -> {
                item.isVisible = false
                menuItemDeleteChecked.isVisible = false
                adapter.checkAll(false)
                adapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLongClick(position: Int) {
        val note = adapter.currentList[position]
        note.isChecked = true
        adapter.notifyDataSetChanged()
        menuItemCheckAll.isVisible = true
        menuItemDeleteChecked.isVisible = true
    }

    override fun onClick(position: Int) {
        val currentNote = adapter.currentList[position]
        val editNoteIntent = Intent(this, AddEditNoteActivity::class.java)
        editNoteIntent.putExtra("MODE", IntentCodes.INFO)
        editNoteIntent.putExtra("TITLE", currentNote.title)
        editNoteIntent.putExtra("DESCRIPTION", currentNote.description)
        editNoteIntent.putExtra("IMAGE", currentNote.imageUri)
        editNoteIntent.putExtra("CREATED_DATE", currentNote.createdDate)
        editNoteIntent.putExtra("UPDATED_DATE", currentNote.updatedDate)
        editNoteIntent.putExtra("ID", currentNote.id)
        this.startActivity(editNoteIntent)
    }
}