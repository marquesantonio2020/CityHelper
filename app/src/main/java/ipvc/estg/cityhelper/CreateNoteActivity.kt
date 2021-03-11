package ipvc.estg.cityhelper

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cityhelper.NoteDescriptionActivity.Companion.SENDING_DESCRIPTION
import ipvc.estg.cityhelper.NoteDescriptionActivity.Companion.SENDING_TITLE
import ipvc.estg.cityhelper.adapters.NOTE_ID
import ipvc.estg.cityhelper.viewModel.NoteViewModel

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var createNoteTitle: EditText
    private lateinit var createNoteDescription: EditText
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        createNoteTitle = findViewById(R.id.note_create_title_input)
        createNoteDescription = findViewById(R.id.note_create_description_input)

        var createBtn = findViewById<Button>(R.id.btn_create)
        var backBtn = findViewById<Button>(R.id.btn_back)

        backBtn.setOnClickListener{
            finish()
        }

        Toast.makeText(this, "${intent.getStringExtra(SENDING_DESCRIPTION)}", Toast.LENGTH_LONG).show()
        if(intent.getStringExtra(SENDING_TITLE).isNullOrEmpty()){
            createBtn.setOnClickListener{
                val replyIntent = Intent()

                if(TextUtils.isEmpty(createNoteTitle.text) || TextUtils.isEmpty(createNoteDescription.text)){
                    if(TextUtils.isEmpty(createNoteTitle.text) && !TextUtils.isEmpty(createNoteDescription.text)){
                        placeError(R.id.note_create_title)
                        resetError(R.id.note_create_description)
                    }
                    if(!TextUtils.isEmpty(createNoteTitle.text) && TextUtils.isEmpty(createNoteDescription.text)){
                        placeError(R.id.note_create_description)
                        resetError(R.id.note_create_title)
                    }
                    if(TextUtils.isEmpty(createNoteTitle.text) && TextUtils.isEmpty(createNoteDescription.text)){
                        placeError(R.id.note_create_title)
                        placeError(R.id.note_create_description)
                    }
                    Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_LONG).show()
                } else{
                    val noteTitle = createNoteTitle.text.toString()
                    val noteDescription = createNoteDescription.text.toString()

                    replyIntent.putExtra(TITLE, noteTitle)
                    replyIntent.putExtra(DESCRIPTION, noteDescription)

                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                }
            }
        }else {
            createBtn.setText(R.string.btn_edit)

            createNoteTitle.setText(intent.getStringExtra(SENDING_TITLE), TextView.BufferType.EDITABLE)
            createNoteDescription.setText(intent.getStringExtra(SENDING_DESCRIPTION), TextView.BufferType.EDITABLE)

            val noteId = intent.getIntExtra(NOTE_ID, 0)

            createBtn.setOnClickListener {
                val replyIntent = Intent()

                if (TextUtils.isEmpty(createNoteTitle.text) || TextUtils.isEmpty(
                        createNoteDescription.text
                    )
                ) {
                    if (TextUtils.isEmpty(createNoteTitle.text) && !TextUtils.isEmpty(
                            createNoteDescription.text
                        )
                    ) {
                        placeError(R.id.note_create_title)
                        resetError(R.id.note_create_description)
                    }
                    if (!TextUtils.isEmpty(createNoteTitle.text) && TextUtils.isEmpty(
                            createNoteDescription.text
                        )
                    ) {
                        placeError(R.id.note_create_description)
                        resetError(R.id.note_create_title)
                    }
                    if (TextUtils.isEmpty(createNoteTitle.text) && TextUtils.isEmpty(
                            createNoteDescription.text
                        )
                    ) {
                        placeError(R.id.note_create_title)
                        placeError(R.id.note_create_description)
                    }
                    Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_LONG).show()
                } else {
                    val noteTitle = createNoteTitle.text.toString()
                    val noteDescription = createNoteDescription.text.toString()
                    noteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(NoteViewModel::class.java)
                    noteViewModel.updateNoteById(noteId, noteTitle, noteDescription)

                    finish()

                }
            }


        }
    }

    companion object{
        const val TITLE = "Title"
        const val DESCRIPTION = "Description"
        const val TOEDIT = "to_edit"
    }

    private fun placeError(viewID: Int){
        var targetView = findViewById<TextView>(viewID)

        targetView.setTextColor(ContextCompat.getColor(this, R.color.errorTextColor))

    }

    private fun resetError(viewID: Int){
        var targetView = findViewById<TextView>(viewID)

        targetView.setTextColor(ContextCompat.getColor(this, R.color.defaultTextColor))

    }

    fun createNote(view: View) {}
}