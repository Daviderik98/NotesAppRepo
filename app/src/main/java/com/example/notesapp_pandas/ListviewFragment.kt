package com.example.notesapp_pandas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.notesapp_pandas.databinding.FragmentFirstBlankBinding
import com.example.notesapp_pandas.databinding.FragmentListviewBinding


class ListviewFragment : Fragment() {

    private lateinit var notesList: MutableList<String>
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: ArrayAdapter<String>
    private lateinit var titleInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveButton: Button

    private var _binding: FragmentListviewBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListviewBinding.inflate(layoutInflater, container, false)
        val listView = binding.root

        notesList = mutableListOf()
        notesListView = binding.notesList
        titleInput = binding.titlesInput
        notesInput = binding.notesInput
        saveButton  = binding.saveButton

        notesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notesList)
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
        // Inflate the layout for this fragment
        return listView
    }


}