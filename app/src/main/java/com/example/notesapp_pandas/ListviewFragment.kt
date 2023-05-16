package com.example.notesapp_pandas


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesapp_pandas.databinding.FragmentListviewBinding

  // TODO LoginFragment navigates to ListActivity , Task from K & M
class ListviewFragment : Fragment(){
    private var _binding: FragmentListviewBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListviewBinding.inflate(layoutInflater, container,false)
         val view = binding.root


        // TODO Navigate to ListActivity from K & M
       // val intentNavigate =  Intent(activity, ListActivity::class.java)
       // startActivity(intentNavigate)


        return view
    }


      override fun onDestroy() {
          super.onDestroy()
          _binding = null
      }


}