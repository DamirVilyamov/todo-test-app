package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        initRecyclerView()

        button_add_note.setOnClickListener {
            val addNoteIntent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra("MODE", IntentCodes.ADD)
            startActivity(addNoteIntent)
        }

    }

    private fun initRecyclerView() {
        recycler_view_notes.layoutManager = LinearLayoutManager(this)
        val adapter = NotesAdapter()
        recycler_view_notes.adapter = adapter
        noteViewModel.getAllNotes()?.observe(this, { notes -> //update recyclerview here
            adapter.submitList(notes)
        })

    }


    /* val touchHelper = ItemTouchHelper(
     object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
         override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
             return false
         }

         override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
             val adapter = viewHolder.
             noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
             Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
         }
     }).attachToRecyclerView(recycler_view_notes)*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes' been deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

}