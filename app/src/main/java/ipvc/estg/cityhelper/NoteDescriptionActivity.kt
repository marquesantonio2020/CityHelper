package ipvc.estg.cityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cityhelper.adapters.NOTE_ID
import ipvc.estg.cityhelper.viewModel.NoteViewModel

private lateinit var noteViewModel : NoteViewModel
private lateinit var noteTitle: TextView
private lateinit var noteDescription: TextView
private lateinit var noteId: TextView
private lateinit var deleteBtn: Button
private lateinit var editBtn: Button
private lateinit var backBtn: Button

class NoteDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_description)

        noteId = findViewById(R.id.noteId)
        noteTitle = findViewById(R.id.note_description_title)
        noteDescription = findViewById(R.id.note_description_description)

        deleteBtn = findViewById(R.id.note_description_delete_btn)
        editBtn = findViewById(R.id.note_description_edit_btn)
        backBtn = findViewById(R.id.note_description_back_btn)

        backBtn.setOnClickListener{
            finish()
        }

        //Retrieve param from UserNoteListActivity
        var selectedNoteTitle = intent.getIntExtra(NOTE_ID, 0)

        /**View Model for Note Entity***********************/
        noteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(NoteViewModel::class.java)
        /**********************************/

        /**Injecting data about the selected note*/
        noteViewModel.getNoteById(selectedNoteTitle!!).observe(this, Observer { note ->
            note?.let {
                noteId.text = it.id.toString()
                noteTitle.text = it.title
                noteDescription.text = it.description
            }
        })
        /*****************************************/



        /**Delete button action***********************/
        deleteBtn.setOnClickListener{
            deleteNote(selectedNoteTitle!!)
        }
        /********************************************/

        /**Edit button action***********************/
        editBtn.setOnClickListener{
            var intent = Intent(this, CreateNoteActivity::class.java)

            //Toast.makeText(this, "${noteTitle.text}", Toast.LENGTH_LONG).show()
            intent.putExtra(NOTE_ID, selectedNoteTitle)
            intent.putExtra(SENDING_TITLE, noteTitle.text.toString())
            intent.putExtra(SENDING_DESCRIPTION, noteDescription.text.toString())

            startActivity(intent)
        }
        /********************************************/
    }

    private fun deleteNote(selectedNoteTitle: Int){
        noteViewModel.deleteById(selectedNoteTitle)
        Toast.makeText(this, R.string.successful_delete, Toast.LENGTH_LONG).show()
        finish()
    }

    companion object{
        const val SENDING_TITLE = "sending_title"
        const val SENDING_DESCRIPTION = "sending_description"
    }

}

