package com.example.notesapp_pandas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.notesapp_pandas.databinding.ActivityListBinding
import com.example.notesapp_pandas.databinding.ActivityMainBinding
import yuku.ambilwarna.AmbilWarnaDialog

class ListActivity : AppCompatActivity() {

    private lateinit var notesList: MutableList<String>
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: ArrayAdapter<String>
    private lateinit var titleInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var saveButton: Button
    private lateinit var imageButton: ImageButton
    private lateinit var sizePicker: NumberPicker

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
        imageButton = findViewById(R.id.imageButton2)
        sizePicker = findViewById(R.id.numberPicker)

        sizePicker.minValue = 12
        sizePicker.maxValue = 30
        sizePicker.wrapSelectorWheel = false


           // notesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
        notesAdapter = object : ArrayAdapter<String>(
            this,
            R.layout.list_item,
            R.id.text1,
            notesList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val titleTextView = view.findViewById<TextView>(R.id.text1)

                val textSize = sizePicker.value.toFloat()
                titleTextView.textSize = textSize

                return view
            }
        }
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
        imageButton.setOnClickListener{
            val textSize = sizePicker.value.toFloat()
            titleInput.textSize = textSize
            notesInput.textSize = textSize
        }
    }
    fun changeColor(view: View){
        val initialColor = titleInput.currentTextColor

        val colorPickerDialog = AmbilWarnaDialog(this, initialColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
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






