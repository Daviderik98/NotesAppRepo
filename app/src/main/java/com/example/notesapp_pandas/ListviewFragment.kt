package com.example.notesapp_pandas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yuku.ambilwarna.AmbilWarnaDialog
import android.widget.*

import com.example.notesapp_pandas.databinding.FragmentFirstBlankBinding
import com.example.notesapp_pandas.databinding.FragmentListviewBinding


class ListviewFragment : Fragment() {

    private lateinit var notesList: MutableList<String>
    private lateinit var textSizeList: MutableList<Float>
    private lateinit var textColorList: MutableList<Int>
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: ArrayAdapter<String>
    private lateinit var notesInput: EditText
    private lateinit var titleInput: EditText
    private lateinit var saveButton: Button
    private lateinit var imageButton: ImageView
    private lateinit var sizePicker: NumberPicker

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
        textSizeList = mutableListOf()
        textColorList = mutableListOf()
        notesListView = binding.notesList
        notesInput = binding.notesInput
        saveButton  = binding.saveButton
        titleInput = binding.titlesInput
        imageButton = binding.imgSetTextsize
        sizePicker = binding.numberPicker

        sizePicker.minValue = 16
        sizePicker.maxValue = 30
        sizePicker.wrapSelectorWheel = false

        //notesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, notesList)
        //notesListView.adapter = notesAdapter

        notesAdapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.list_item,
            R.id.item_text_note,
            notesList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val titleTextView = view.findViewById<TextView>(R.id.item_text_note)

                val textSize = textSizeList[position]
                val textColor = textColorList[position]
                titleTextView.textSize = textSize
                titleTextView.setTextColor(textColor)

                return view
            }
        }

        notesListView.adapter = notesAdapter

        saveButton.setOnClickListener {
            val newNoteCard = titleInput.text.toString() + "\n" + notesInput.text.toString()
            if (newNoteCard.isNotEmpty()) {
                notesList.add(newNoteCard)
                val textSize = sizePicker.value.toFloat()
                val textColor = titleInput.currentTextColor
                textSizeList.add(textSize)
                textColorList.add(textColor)
                notesAdapter.notifyDataSetChanged()
                titleInput.text.clear()
                notesInput.text.clear()
            }
        }

        val editBtn = binding.imageEditcolor

        editBtn.setOnClickListener() {
            changeColor(listView)
        }

        imageButton.setOnClickListener{
            val textSize = sizePicker.value.toFloat()
            titleInput.textSize = textSize
            notesInput.textSize = textSize
        }
        // Inflate the layout for this fragment
        return listView
    }

    private fun changeColor(view: View) {
        val initialColor = titleInput.currentTextColor

        val colorPickerDialog = AmbilWarnaDialog(requireContext(), initialColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog?) {
                // Handle dialog cancellation
            }

            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                titleInput.setTextColor(color)
                notesInput.setTextColor(color)
            }
        })
        colorPickerDialog.show()
    }

}