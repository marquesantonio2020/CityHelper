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
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var createNoteTitle: EditText
    private lateinit var createNoteDescription: EditText
    private lateinit var createNoteTitleTextView : TextView
    private lateinit var createNoteDescriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        createNoteTitle = findViewById(R.id.note_create_title_input)
        createNoteDescription = findViewById(R.id.note_create_description_input)

        var createBtn = findViewById<Button>(R.id.btn_create)

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
    }

    companion object{
        const val TITLE = "Title"
        const val DESCRIPTION = "Description"
    }

    fun placeError(viewID: Int){
        var targetView = findViewById<TextView>(viewID)

        targetView.setTextColor(ContextCompat.getColor(this, R.color.errorTextColor))

    }

    fun resetError(viewID: Int){
        var targetView = findViewById<TextView>(viewID)

        targetView.setTextColor(ContextCompat.getColor(this, R.color.defaultTextColor))

    }

    fun createNote(view: View) {}
}