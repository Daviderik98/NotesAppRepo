package com.example.notesapp_pandas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation

import com.example.notesapp_pandas.databinding.FragmentFirstBlankBinding
import com.example.notesapp_pandas.databinding.FragmentListviewBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import yuku.ambilwarna.AmbilWarnaDialog


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

    private val db: DatabaseReference =
        FirebaseDatabase.getInstance("https://database-pandanotes-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

    private var _binding: FragmentListviewBinding? = null
    private val binding get() = _binding!!
    val userViewModel: UserViewModel by activityViewModels()
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
        saveButton = binding.saveButton
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





            val currentUser = userViewModel.currentUser.value
            if (currentUser == null) {
                Toast.makeText(activity, "No current user", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val titlesInput = titleInput.text.toString()
            val bloggInput = notesInput.text.toString()



            if (titlesInput.isNotEmpty()&& bloggInput.isNotEmpty()){
                val uniqueId: String? = currentUser?.let { it1 -> db.child(it1.userId).child("anteckningar").push().key }
                    ?: ""

                val newObj = uniqueId?.let { it1 -> UserNotes(title = titlesInput, blogg = bloggInput, uniqueID = it1) }

                if (currentUser != null) {
                    if (uniqueId != null) {
                        db.child(currentUser.userId).child("anteckningar").child(uniqueId).setValue(newObj).addOnCompleteListener{task ->
                            if (task.isSuccessful){
                                //notesList[uniqueId] = newObj.toString()
                                //notesAdapter.clear()
                                Toast.makeText(activity,"Notes saved",Toast.LENGTH_SHORT).show()

                            }else{
                                Toast.makeText(activity,"failed to save!",Toast.LENGTH_SHORT).show()
                            }

                        }
                        val noteDisplayText = "$titlesInput - $bloggInput"
                        notesList.add(noteDisplayText)
                        val textSize = sizePicker.value.toFloat()
                        val textColor = titleInput.currentTextColor
                        textSizeList.add(textSize)
                        textColorList.add(textColor)
                        notesAdapter.notifyDataSetChanged()
                    }
                }
                //notesList.add(titleInput)
                // notesList.add(bloggInput)
                // notesAdapter.notifyDataSetChanged()

// titleInput.text.clear()
// notesInput.text.clear()

            }
            notesListView.setOnItemLongClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as String
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setMessage("Are you sure you want to delete this note")
                    .setCancelable(false)
                    .setPositiveButton("Yes") {_, _ ->
                        notesList.removeAt(position)
                        notesAdapter.notifyDataSetChanged()

                    }
                    .setNegativeButton("No"){dialog, _ ->
                        dialog.cancel()
                    }
                val alert = dialogBuilder.create()
                alert.setTitle("Delete note")
                alert.show()
                true


            }
            // TODO navigate to SearchNotesFragment K & M
            notesListView.setOnItemClickListener { parent, view, position, id->
                val item = parent.getItemAtPosition(position) as String
                Navigation.findNavController(view)
                    .navigate(R.id.action_listviewFragment_to_searchNotesFragment)

            }
        }
        imageButton.setOnClickListener {
            val currentTextSize = sizePicker.value.toFloat()
            titleInput.textSize = currentTextSize
            notesInput.textSize = currentTextSize
        }
        val editBtn = binding.imageEditcolor
        editBtn.setOnClickListener {
            changeColor(listView)
        }
        // Inflate the layout for this fragment
        return listView
    }
    fun changeColor(view: View) {
        val initialColor = titleInput.currentTextColor


        val colorPickerDialog = AmbilWarnaDialog(
            requireContext(),
            initialColor,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    //Handle dialog cancellation
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    titleInput.setTextColor(color)
                    notesInput.setTextColor(color)
                }
            })
        colorPickerDialog.show()
    }
}