package ipvc.estg.cityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cityhelper.adapters.NOTE_ID
import ipvc.estg.cityhelper.viewModel.NoteViewModel

private lateinit var noteViewModel : NoteViewModel
private lateinit var noteTitle: TextView
private lateinit var noteDescription: TextView

class NoteDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_description)
        
        noteTitle = findViewById(R.id.note_description_title)
        noteDescription = findViewById(R.id.note_description_description)
        /**View Model for Note Entity***********************/



        /**********************************/







    }
}

