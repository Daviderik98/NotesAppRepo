package com.example.notesapp_pandas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.notesapp_pandas.databinding.FragmentFirstBlankBinding


class FirstBlankFragment : Fragment() {
  private var _binding: FragmentFirstBlankBinding?= null
  private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBlankBinding.inflate(layoutInflater, container, false)
        val firstblankView = binding.root

        val btnToAbout = binding.btnAbout
        btnToAbout.setOnClickListener{
            Navigation.findNavController(firstblankView).navigate(R.id.action_firstBlankFragment_to_aboutFragment)
        }

        val btnToRegister = binding.btnSignIn
        btnToRegister.setOnClickListener{
            Navigation.findNavController(firstblankView).navigate(R.id.action_firstBlankFragment_to_loginFragment)
        }

        return firstblankView
    }


}