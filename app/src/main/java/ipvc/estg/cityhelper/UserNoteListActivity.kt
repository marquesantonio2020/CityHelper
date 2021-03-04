package ipvc.estg.cityhelper

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.NoteListAdapter
import ipvc.estg.cityhelper.dataclasses.NoteDataClass
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.viewModel.NoteViewModel
import kotlinx.android.synthetic.main.activity_user_note_list_acitivity.*

class UserNoteListActivity : AppCompatActivity() {

    private lateinit var noteViewModel : NoteViewModel
    private lateinit var addNoteBtn: View
    private lateinit var noteTitle: String
    private lateinit var noteDescription: String
    private val newNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_note_list_acitivity)

        //Applies back button to Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.notes)

        /** Data Injection to Recycler View*/
        val adapter = NoteListAdapter(this)
        //Gives recycler view the created adapter
        note_recycler_view.adapter = adapter
        note_recycler_view.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)

        /***********************************/

        /**View Model***********************/

        noteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            //Update the cached copy of the words in the adapter
            notes?.let { adapter.setNotes(it) } })

        /**********************************/

        /**Floating button action *********/

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

        Log.d("TESTE", "${requestCode} e ${resultCode}" )

        if(requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK){
            data?.getStringExtra(CreateNoteActivity.TITLE)?.let{
                noteTitle = it
            }
            data?.getStringExtra(CreateNoteActivity.DESCRIPTION)?.let{
                noteDescription = it
            }
            val insertedNote = Note(title = noteTitle, description = noteDescription)
            noteViewModel.insert(insertedNote)
        } else{
            Toast.makeText(applicationContext,
            "Nota encontra-se vazia",
            Toast.LENGTH_LONG).show()
        }
    }
}