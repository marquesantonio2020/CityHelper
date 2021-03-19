package ipvc.estg.cityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

private lateinit var citySpinner: Spinner
private lateinit var titleInput: EditText
private lateinit var streetInput: EditText
private lateinit var descriptionInput: EditText

class CreateReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_report)

        titleInput = findViewById(R.id.report_create_title_input)
        streetInput = findViewById(R.id.report_create_location_input)
        citySpinner = findViewById(R.id.city_spinner)
        descriptionInput = findViewById(R.id.report_create_description_input)

        val languages = resources.getStringArray(R.array.Languages)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)

        citySpinner.adapter = adapter

    }
}