package ipvc.estg.cityhelper

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.CreateNoteActivity.Companion.TOEDIT
import ipvc.estg.cityhelper.adapters.NOTE_ID
import ipvc.estg.cityhelper.adapters.NoteListAdapter
import ipvc.estg.cityhelper.dataclasses.NoteDataClass
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.viewModel.NoteViewModel
import kotlinx.android.synthetic.main.activity_user_note_list_acitivity.*

class UserNoteListActivity : AppCompatActivity(), NoteListAdapter.NoteElementInterface{

    private lateinit var noteViewModel : NoteViewModel
    private lateinit var addNoteBtn: View
    private lateinit var noteTitle: String
    private lateinit var noteDescription: String
    private lateinit var noteId: String
    private var gridSpan: Int = 2
    private var orientation: Int = 0
    private val newNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_note_list_acitivity)

        orientation = resources.configuration.orientation

        //Applies back button to Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.notes)

        /** Data Injection to Recycler View*/
        val adapter = NoteListAdapter(this, this)
        //Gives recycler view the created adapter
        note_recycler_view.adapter = adapter
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            note_recycler_view.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        }else{
            note_recycler_view.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        }
        /***********************************/

        /**View Model for Note Entity***********************/

        noteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            //Update the cached copy of the words in the adapter
            notes?.let { adapter.setNotes(it) } })

        /**********************************/

        /**Floating button action - Add Note*/

        addNoteBtn = findViewById(R.id.btn_addNote)

        addNoteBtn.setOnClickListener{
            val intent = Intent(this@UserNoteListActivity, CreateNoteActivity::class.java)

            startActivityForResult(intent, newNoteActivityRequestCode)
        }

        /**********************************/


    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(INTENT_PARAM, "reports")
        }
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK){
            data?.getStringExtra(CreateNoteActivity.TITLE)?.let{
                noteTitle = it
            }
            data?.getStringExtra(CreateNoteActivity.DESCRIPTION)?.let{
                noteDescription = it
            }
            if(data?.getBooleanExtra(TOEDIT, false)!!){
                val id: Int = data?.getIntExtra(NOTE_ID, 0)
                noteViewModel.updateNoteById(id, noteTitle, noteDescription)
            }else{
                val insertedNote = Note(title = noteTitle, description = noteDescription)
                noteViewModel.insert(insertedNote)

                Toast.makeText(applicationContext,
                    R.string.successful_create,
                    Toast.LENGTH_LONG).show()
            }
        } else{
            Toast.makeText(applicationContext,
            R.string.missing_fields,
            Toast.LENGTH_LONG).show()
        }
    }

    override fun returnNoteTitle(noteId: Int) {
        noteViewModel.deleteById(noteId)
        Toast.makeText(this, R.string.successful_delete, Toast.LENGTH_LONG).show()
    }

    override fun editNoteById(noteId: Int, noteTitle: String, noteDescription: String) {
        var intent = Intent(this, CreateNoteActivity::class.java)

        //Toast.makeText(this, "${noteTitle.text}", Toast.LENGTH_LONG).show()
        intent.putExtra(NOTE_ID, noteId)
        intent.putExtra(NoteDescriptionActivity.SENDING_TITLE, noteTitle)
        intent.putExtra(NoteDescriptionActivity.SENDING_DESCRIPTION, noteDescription)

        startActivity(intent)
    }

}