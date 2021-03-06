package ipvc.estg.cityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cityhelper.adapters.NOTE_TITLE
import ipvc.estg.cityhelper.viewModel.NoteViewModel

private lateinit var noteViewModel : NoteViewModel
private lateinit var noteTitle: TextView
private lateinit var noteDescription: TextView
private lateinit var deleteBtn: Button
private lateinit var editBtn: Button

class NoteDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_description)
        
        noteTitle = findViewById(R.id.note_description_title)
        noteDescription = findViewById(R.id.note_description_description)

        deleteBtn = findViewById(R.id.note_description_delete_btn)

        //Retrieve param from UserNoteListActivity
        var selectedNoteTitle = intent.getStringExtra(NOTE_TITLE)

        /**View Model for Note Entity***********************/
        noteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(NoteViewModel::class.java)
        /**********************************/

        /**Delete button action***********************/
        deleteBtn.setOnClickListener{
            deleteNote(selectedNoteTitle!!)
        }
        /********************************************/
    }

    private fun deleteNote(selectedNoteTitle: String){
        noteViewModel.deleteByTitle(selectedNoteTitle)
        Toast.makeText(this, R.string.successful_delete, Toast.LENGTH_LONG).show()
        finish()
    }
}

