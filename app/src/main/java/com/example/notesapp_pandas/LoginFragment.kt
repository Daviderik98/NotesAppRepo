package com.example.notesapp_pandas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.notesapp_pandas.databinding.FragmentLoginBinding
import com.example.notesapp_pandas.databinding.FragmentRegisterBinding
import com.google.firebase.database.*


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get()= _binding!!
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val loginView = binding.root
        db = FirebaseDatabase.getInstance("https://database-pandanotes-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")

        val enteredName = binding.editTextEnterUser
        val enteredPass = binding.editTextEnterPass
        val btnSubmit = binding.submission
        val btnRegister = binding.toSignUp

        btnSubmit.setOnClickListener{
            val inputUsername = enteredName.text.toString()
            val inputPassword = enteredPass.text.toString()


            db.orderByChild("username").equalTo(inputUsername).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){

                        for (userSnapshot in snapshot.children){

                            val user = userSnapshot.getValue(User::class.java)

                            if (user != null && user.password == inputPassword){
                                val currentUser = userSnapshot.key?.let {
                                        it1->
                                    User(
                                        username = user.username,
                                        password=user.password,
                                        userId = it1
                                    )
                                    //todo : viewmodel for fething the current user
                                }
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, "BIG TIME ERROR", Toast.LENGTH_SHORT).show()
                }

            }) // TODO navigate to ListviewFragment from K & M
           // Navigation.findNavController(loginView).navigate(R.id.action_loginFragment_to_listviewFragment2)

            // TODO Navigate to ListActivity from K & M
            val intentNavigate =  Intent(activity, ListActivity::class.java)
            startActivity(intentNavigate)

    }

        btnRegister.setOnClickListener{
            Navigation.findNavController(loginView).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return loginView
    }
}