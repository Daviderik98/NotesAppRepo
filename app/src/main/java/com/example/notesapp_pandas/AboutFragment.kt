package com.example.notesapp_pandas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.notesapp_pandas.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(layoutInflater, container, false)
        val aboutView = binding.root

        val btnBackArrow = binding.imgBtnBackarrow

        btnBackArrow.setOnClickListener{
            Navigation.findNavController(aboutView).navigate(R.id.action_aboutFragment_to_firstBlankFragment)
        }

        return aboutView
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_about, container, false)
    }


}