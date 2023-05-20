package com.example.notesapp_pandas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel: ViewModel() {
    //current user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    //currentUser ID
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    //get the current User with the ID
    fun getCurrentUser(user: User?) {
        _currentUser.value = user

        _currentUserId.value = user?.userId
        if (user != null) {
            fetchNotes(user.userId)
        }

    }

    private val _notesState = MutableStateFlow<UiState<List<UserNotes>>>(UiState.Empty)
    val notesState: StateFlow<UiState<List<UserNotes>>> = _notesState.asStateFlow()

    fun fetchNotes(userId: String) {
        viewModelScope.launch {
            _notesState.value = UiState.Loading
            val db: DatabaseReference =
                FirebaseDatabase.getInstance("https://database-pandanotes-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Users")
            val notesSnapshot = db.child(userId).child("anteckningar").get().await()
            if (notesSnapshot.exists()) {
                val notesMap = notesSnapshot.children.mapNotNull { it.getValue(UserNotes::class.java) }
                if (notesMap.isEmpty()) {
                    _notesState.value = UiState.Empty
                } else {
                    _notesState.value = UiState.Success(notesMap)
                }
            } else {
                _notesState.value = UiState.Error(Exception("No notes found for the user"))
            }
        }
    }


    }
