package com.example.notesapp_pandas

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel: ViewModel() {
    //current user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    //current users Id
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    //get the current User with the ID
    fun getCurrentUser(user: User?){
        _currentUser.value = user

        _currentUserId.value = user?.userId
    }
}