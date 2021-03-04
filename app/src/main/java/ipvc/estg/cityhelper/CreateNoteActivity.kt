package ipvc.estg.cityhelper

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var editNoteTitle: EditText
    private lateinit var editNoteDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        editNoteTitle = findViewById(R.id.note_create_title_input)
        editNoteDescription = findViewById(R.id.note_create_description_input)

        var createBtn = findViewById<Button>(R.id.btn_create)

        createBtn.setOnClickListener{
            val replyIntent = Intent()

            if(TextUtils.isEmpty(editNoteTitle.text) || TextUtils.isEmpty(editNoteDescription.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else{
                val noteTitle = editNoteTitle.text.toString()
                val noteDescription = editNoteDescription.text.toString()

                replyIntent.putExtra(TITLE, noteTitle)
                replyIntent.putExtra(DESCRIPTION, noteDescription)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object{
        const val TITLE = "Title"
        const val DESCRIPTION = "Description"
    }

    fun createNote(view: View) {}
}