package com.example.notesapp_pandas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.notesapp_pandas.databinding.FragmentRegisterBinding
import com.example.notesapp_pandas.databinding.FragmentSearchNotesBinding

class SearchNotesFragment : Fragment() {
    private var _binding: FragmentSearchNotesBinding? = null
    //private val binding get() = _binding!!
    private val binding get() = _binding!!
    private lateinit var titleList: MutableList<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentSearchNotesBinding.inflate(layoutInflater,container, false)
        val view = binding.root

        val goBackToListview = binding.imgArrowGoToListview
        goBackToListview.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_searchNotesFragment_to_listviewFragment)
        }





        val searchTitle = binding.etSearchTitle.toString()

        if (searchTitle.isBlank()){

        }




        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}