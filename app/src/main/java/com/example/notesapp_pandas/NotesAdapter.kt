package com.example.notesapp_pandas
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.DatabaseReference

class NotesAdapter(
    private val context: Context,
    private val dataSource: MutableList<UserNotes>,
    private val db: DatabaseReference,
    private val currentUser: User?
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.list_item, parent, false)

        val titleTextView = view.findViewById<TextView>(R.id.item_text_note)
        val deleteButton = view.findViewById<ImageView>(R.id.btn_trashcan)

        val note = getItem(position) as UserNotes
        titleTextView.text = "${note.title} - ${note.blogg}"
        titleTextView.textSize = note.textSize
        titleTextView.setTextColor(note.textColor)

        deleteButton.setOnClickListener {
            val noteId = note.uniqueID
            if (noteId != null && currentUser != null) {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setMessage("Are you sure you want to delete this note")
                    .setCancelable(false)
                    .setPositiveButton("Yes") {_, _ ->
                        //notesList.removeAt(position)
                        //notesAdapter.notifyDataSetChanged()
                        db.child(currentUser.userId).child("anteckningar").child(noteId).removeValue()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                                    dataSource.remove(note)
                                    notifyDataSetChanged()
                                } else {
                                    Toast.makeText(context, "Failed to delete note", Toast.LENGTH_SHORT).show()
                                }
                            }

                    }
                    .setNegativeButton("No"){dialog, _ ->
                        dialog.cancel()
                    }
                val alert = dialogBuilder.create()
                alert.setTitle("Delete note")
                alert.show()
                true



            }
        }



        return view
    }
}