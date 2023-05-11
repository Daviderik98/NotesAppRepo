package com.example.notesapp_pandas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.notesapp_pandas.databinding.ActivityListBinding
import com.example.notesapp_pandas.databinding.ActivityMainBinding

class ListActivity : AppCompatActivity() {

    private lateinit var notesList: MutableList<String>
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: ArrayAdapter<String>
    private lateinit var titleInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveButton: Button

    private lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)


            notesList = mutableListOf()
            notesListView = findViewById(R.id.notes_list)
            titleInput = findViewById(R.id.titles_input)
            notesInput = findViewById(R.id.notes_input)
            saveButton  = findViewById(R.id.save_button)

            notesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
            notesListView.adapter = notesAdapter

            saveButton.setOnClickListener {
                val newNoteCard = "\n" + "Title: " + titleInput.text.toString() +
                        "\n" + "Note: " + notesInput.text.toString() + "\n"
                if (newNoteCard.isNotEmpty()) {

                    notesList.add(newNoteCard)
                    notesAdapter.notifyDataSetChanged()
                    titleInput.text.clear()
                    notesInput.text.clear()
                }
            }
        }
    }






