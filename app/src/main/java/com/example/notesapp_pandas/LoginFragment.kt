package com.example.notesapp_pandas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.notesapp_pandas.databinding.FragmentLoginBinding
import com.example.notesapp_pandas.databinding.FragmentRegisterBinding
import com.google.firebase.database.*


class LoginFragment : Fragment() {
    val userViewModel: UserViewModel by activityViewModels()
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
            var inputUsername = enteredName.text.toString()
            var inputPassword = enteredPass.text.toString()

            if(inputUsername.isNotEmpty() && inputPassword.isNotEmpty()){
                db.orderByChild("username").equalTo(inputUsername).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){

                            for (userSnapshot in snapshot.children){

                                val user: User? = userSnapshot.getValue(User::class.java)
                                //Pro-tip don´t confuse snapShot with userSnapshot
                                //val anteckningarMap: Map<String, UserNotes>? = user?.anteckningar


                                if (user != null && user.password == inputPassword){
                                    println("SECOND CHANCE TO TURN BACK?!")
                                    val anteckningarMap = user.anteckningar
                                    val currentUser = userSnapshot.key?.let { it1 ->
                                        User(
                                            username = user.username,
                                            password = user.password,
                                            userId = it1, anteckningar = anteckningarMap

                                        )
                                    }
                                    userViewModel.getCurrentUser(currentUser)
                                    if (currentUser != null) {
                                        println(currentUser.username)
                                    }
                                    //val anteckningarList: List<UserNotes>? = anteckningarMap?.values?.toList()
                                    //val anteckningarList = mutableListOf<UserNotes>()
                                    //val anteckningarSnapshot = userSnapshot.child("anteckningar")

                                    // for (noteSnapshot in anteckningarSnapshot.children) {
                                    //  val note = noteSnapshot.getValue(UserNotes::class.java)
                                    //  if (note != null) {
                                    //     anteckningarList.add(note)
                                    //   }
                                    // }

                                    //userViewModel.getCurrentUser(currentUser)
                                    Navigation.findNavController(loginView).navigate(R.id.action_loginFragment_to_listviewFragment)
                                }
                            }

                        }
                        else{
                            Toast.makeText(activity, "This user doesnt exist", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(activity, "ERROR 404 NOT FOUND", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            else{
                Toast.makeText(activity, "You must fill in both fields to pass. Those are the rules", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener{
            Navigation.findNavController(loginView).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return loginView
    }
}