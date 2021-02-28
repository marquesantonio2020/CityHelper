package ipvc.estg.cityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.NoteListAdapter
import ipvc.estg.cityhelper.dataclasses.Note
import ipvc.estg.cityhelper.dataclasses.Report
import kotlinx.android.synthetic.main.activity_user_note_list_acitivity.*
import kotlinx.android.synthetic.main.activity_user_report_list.*

class UserNoteListAcitivity : AppCompatActivity() {

    private lateinit var noteList: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_note_list_acitivity)

        //Applies back button to Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        /** Data Injection to Recycler View*/

        //DataClass Constructor
        noteList = ArrayList<Note>()

        //Example for data injection
        for(i in 0 until 100){
            noteList.add(Note("Title $i", "Description $i"))
        }

        //Gives recycler view the created adapter
        note_recycler_view.adapter = NoteListAdapter(noteList)
        note_recycler_view.layoutManager = LinearLayoutManager(this)

        /***********************************/
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(INTENT_PARAM, "reports")
        }
        startActivity(intent)
    }
}