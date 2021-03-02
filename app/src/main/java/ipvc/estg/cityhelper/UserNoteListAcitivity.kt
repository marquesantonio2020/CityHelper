package ipvc.estg.cityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.NoteListAdapter
import ipvc.estg.cityhelper.dataclasses.NoteDataClass
import kotlinx.android.synthetic.main.activity_user_note_list_acitivity.*

class UserNoteListAcitivity : AppCompatActivity() {

    private lateinit var noteList: ArrayList<NoteDataClass>
    private lateinit var addNoteBtn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_note_list_acitivity)

        //Applies back button to Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.notes)

        /** Data Injection to Recycler View*/

        //DataClass Constructor
        noteList = ArrayList<NoteDataClass>()

        //Example for data injection
        for(i in 0 until 100){
            noteList.add(NoteDataClass("Title $i", "Description $i"))
        }

        //Gives recycler view the created adapter
        note_recycler_view.adapter = NoteListAdapter(noteList)
        note_recycler_view.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)

        /***********************************/

        /**Floating button action *********/

        addNoteBtn = findViewById(R.id.btn_addNote)

        addNoteBtn.setOnClickListener{view ->
            val intent = Intent(this, CreateNoteActivity::class.java)

            startActivity(intent)
        }

        /**********************************/
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(INTENT_PARAM, "reports")
        }
        startActivity(intent)
    }
}