package com.example.notesapp_pandas

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation

import com.example.notesapp_pandas.databinding.FragmentFirstBlankBinding
import com.example.notesapp_pandas.databinding.FragmentListviewBinding
import com.example.notesapp_pandas.databinding.ListItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import yuku.ambilwarna.AmbilWarnaDialog


class ListviewFragment : Fragment() {

    private lateinit var notesList: MutableList<UserNotes>
    private lateinit var textSizeList: MutableList<Float>
    private lateinit var textColorList: MutableList<Int>
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesInput: EditText
    private lateinit var titleInput: EditText
    private lateinit var saveButton: Button
    private lateinit var imageButton: ImageView
    private lateinit var sizePicker: NumberPicker
    private lateinit var currentUser: User
    private lateinit var db: DatabaseReference



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

        notesList = mutableListOf<UserNotes>()
        db = FirebaseDatabase.getInstance("https://database-pandanotes-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")
        currentUser = userViewModel.currentUser.value ?: return listView
        notesAdapter = NotesAdapter(requireContext(), notesList, db, currentUser)
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
                val textSize = sizePicker.value.toFloat()
                val textColor = titleInput.currentTextColor

                val newObj = uniqueId?.let { it1 -> UserNotes(title = titlesInput, blogg = bloggInput, uniqueID = it1, textSize = sizePicker.value.toFloat(),
                textColor = titleInput.currentTextColor) }
                if (newObj != null) {
                    notesList.add(newObj)
                }

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





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUserNotesFromFirebase()
    }
    private fun fetchUserNotesFromFirebase() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            userViewModel.notesState.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        // Handle loading state
                    }
                    is UiState.Success -> {
                        val notes = uiState.data
                        //updateListView(notes)
                        updateListView(notes)
                    }
                    is UiState.Error -> {
                        println("ListviewFragment Error loading notes", uiState.exception)
                    }
                    is UiState.Empty -> {
                        // Handle empty state
                    }
                }
            }
        }
    }

    private fun println(s: String, exception: Exception) {

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





    private fun updateListView(notes: List<UserNotes>) {
        // Clear previous data
        notesList.clear()
        //textSizeList.clear()
        //textColorList.clear()

        // Add the new notes to your ListView
        for (note in notes) {
            notesList.add(note)
          //  val noteDisplayText = "${note.title} - ${note.blogg}"
            //notesList.add(noteDisplayText)

            // You'll need to decide how you want to handle text size and color for each note
            //val textSize = 20f // Your default text size
            //val textColor = Color.BLACK // Your default text color

           // textSizeList.add(textSize)
           // textColorList.add(textColor)
        }

        // Notify the adapter that the data has changed
        notesAdapter.notifyDataSetChanged()
    }



}