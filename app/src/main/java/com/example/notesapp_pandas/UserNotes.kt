package com.example.notesapp_pandas

import android.graphics.Color

data class UserNotes(
    var title: String = "",
    var blogg: String = "",
    var uniqueID: String = "",
    var textSize: Float = 20f,
    var textColor: Int = Color.BLACK

) {
}