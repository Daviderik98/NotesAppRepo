package com.example.notesapp_pandas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.notesapp_pandas.databinding.FragmentRegisterBinding
//import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get()= _binding!!
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        val registerView = binding.root
        db = FirebaseDatabase.getInstance("https://database-pandanotes-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")

        val enterName = binding.editTextUserName
        val enterPass = binding.editTextPassword
        val submit = binding.btnToSignIn

        submit.setOnClickListener{
            val userName = enterName.text.toString()
            val passWord = enterPass.text.toString()
            if(userName.isNotEmpty() && passWord.isNotEmpty()){
                db.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(activity, "Username exists", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val newUserId = db.push().key
                            val newUserNotesMap = mutableMapOf<String, UserNotes>()
                            val newUser = newUserId?.let{User(userName, passWord, newUserId,
                                newUserNotesMap
                            )}
                            db.child(newUserId!!).setValue(newUser).addOnSuccessListener {
                                Toast.makeText(activity, "New User Registered", Toast.LENGTH_SHORT).show()
                                Navigation.findNavController(registerView).navigate(R.id.action_registerFragment_to_loginFragment)
                            }.addOnFailureListener{
                                Toast.makeText(activity, "Failed to Register User", Toast.LENGTH_SHORT)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(activity, "Something went wrong. Please try again, in a few minutes", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                Toast.makeText(activity, "You have to fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
        return registerView
    }
}