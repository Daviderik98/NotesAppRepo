package com.example.notesapp_pandas

data class User(
    var username: String = "", var password:String = "", val userId: String = "",
    var anteckningar: MutableMap<String, UserNotes> = HashMap()
) {
    //todo add variables for list of titles and list of written paragraphs
}