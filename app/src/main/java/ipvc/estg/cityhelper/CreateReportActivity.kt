package ipvc.estg.cityhelper

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import ipvc.estg.cityhelper.api.*
import ipvc.estg.cityhelper.api.endpoints.CountryCityEndpoint
import ipvc.estg.cityhelper.api.endpoints.ReportEndPoint
import ipvc.estg.cityhelper.api.endpoints.TypeEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

private lateinit var citySpinner: Spinner
private lateinit var typeSpinner: Spinner
private lateinit var titleInput: EditText
private lateinit var streetInput: EditText
private lateinit var descriptionInput: EditText
private lateinit var createReport: Button
private lateinit var typeResponse: List<Type>
private lateinit var cityResponse: List<CountryCity>

class CreateReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_report)

        titleInput = findViewById(R.id.report_create_title_input)
        streetInput = findViewById(R.id.report_create_location_input)
        citySpinner = findViewById(R.id.city_spinner)
        typeSpinner = findViewById(R.id.problem_spinner)
        descriptionInput = findViewById(R.id.report_create_description_input)

        if(intent.getStringExtra(REPORT_TITLE).isNullOrEmpty()) {
            /**Preparing request to web service - Getting all Cities available *********/
            val request = ServiceBuilder.buildService(CountryCityEndpoint::class.java)
            val call = request.getAllCities()

            call.enqueue(object : Callback<List<CountryCity>> {
                override fun onResponse(
                    call: Call<List<CountryCity>>,
                    response: Response<List<CountryCity>>
                ) {
                    if (response.isSuccessful) {
                        cityResponse = response.body()!!
                        inflateDataIntoSpinnerCities(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<CountryCity>>, t: Throwable) {
                    Log.v("ReportHERE", "${t.message}")
                    Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })

            /********************************************/

            /**Preparing request to web service - Getting all Types of problems available *********/
            val requestTypes = ServiceBuilder.buildService(TypeEndPoint::class.java)
            val callTypes = requestTypes.getTypesProblems()

            callTypes.enqueue(object : Callback<List<Type>> {
                override fun onResponse(call: Call<List<Type>>, response: Response<List<Type>>) {
                    if (response.isSuccessful) {
                        typeResponse = response.body()!!
                        inflateDataIntoSpinnerTypes(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<Type>>, t: Throwable) {
                    Log.v("ReportHERE", "${t.message}")
                    Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })

            /********************************************/

            /**Report Creation Method **********************/
            createReport = findViewById(R.id.report_create_btn)

            createReport.setOnClickListener {
                createReport(true)
            }
            /***********************************************/
        }else{
            createReport = findViewById(R.id.report_create_btn)
            createReport.setText(R.string.btn_edit)

            /**Preparing request to web service - Getting all Cities available *********/
            val request = ServiceBuilder.buildService(CountryCityEndpoint::class.java)
            val call = request.getAllCities()

            call.enqueue(object : Callback<List<CountryCity>> {
                override fun onResponse(
                    call: Call<List<CountryCity>>,
                    response: Response<List<CountryCity>>
                ) {
                    if (response.isSuccessful) {
                        cityResponse = response.body()!!
                        inflateDataIntoSpinnerCities(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<CountryCity>>, t: Throwable) {
                    Log.v("ReportHERE", "${t.message}")
                    Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })

            /********************************************/

            /**Preparing request to web service - Getting all Types of problems available *********/
            val requestTypes = ServiceBuilder.buildService(TypeEndPoint::class.java)
            val callTypes = requestTypes.getTypesProblems()

            callTypes.enqueue(object : Callback<List<Type>> {
                override fun onResponse(call: Call<List<Type>>, response: Response<List<Type>>) {
                    if (response.isSuccessful) {
                        typeResponse = response.body()!!
                        inflateDataIntoSpinnerTypes(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<Type>>, t: Throwable) {
                    Log.v("ReportHERE", "${t.message}")
                    Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })

            /********************************************/
            inflateDataIntoFields()

            createReport.setOnClickListener{
                createReport(false)
            }



        }
    }

    private fun inflateDataIntoSpinnerCities(spinnerData: List<CountryCity>){
        val cities = ArrayList<String>()
        val iterator = spinnerData.iterator()

        iterator.forEach {
            cities.add(it.city_name)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)

        citySpinner.adapter = adapter
    }

    private fun inflateDataIntoSpinnerTypes(spinnerData: List<Type>){
        val types = ArrayList<String>()

        val iterator = spinnerData.iterator()

        iterator.forEach {
            types.add(it.problem_description)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)

        typeSpinner.adapter = adapter
    }

    private fun inflateDataIntoFields(){
        citySpinner.setSelection(intent.getIntExtra(REPORT_LOCATION, -1))
        typeSpinner.setSelection(intent.getIntExtra(REPORT_TYPE, -1))
        titleInput.setText(intent.getStringExtra(REPORT_TITLE), TextView.BufferType.EDITABLE)
        streetInput.setText(intent.getStringExtra(REPORT_STREET), TextView.BufferType.EDITABLE)
        descriptionInput.setText(intent.getStringExtra(REPORT_DESCRIPTION), TextView.BufferType.EDITABLE)
    }

    private fun createReport(isCreate: Boolean){
        val title = titleInput.text.toString()
        val description = descriptionInput.text.toString()
        val local = streetInput.text.toString()
        val city = citySpinner.selectedItemPosition
        val type = typeSpinner.selectedItemPosition

        //Get user id to put on the report information
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.logindata), Context.MODE_PRIVATE)

        val userID = sharedPref.getInt(getString(R.string.userId), 0)
        val selectedCity = cityResponse.get(city).id
        val selectedType = typeResponse.get(type).id

        if(isCreate) {
            val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
            val call = request.newReport(
                title,
                description,
                2.2222,
                2.2222,
                local,
                userID,
                selectedCity,
                selectedType
            )

            call.enqueue(object : Callback<ServerResponse> {
                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    if (response.isSuccessful) {
                        val insertCompleted = response.body()!!

                        if (insertCompleted.status) {
                            Toast.makeText(
                                this@CreateReportActivity,
                                R.string.report_create_success,
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@CreateReportActivity,
                                R.string.report_create_unsuccess,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }else{
            val reportId = intent.getIntExtra(SELECTED_REPORT, -1)
            val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
            val call = request.updateReport(
                title,
                description,
                2.2222,
                2.2222,
                local,
                userID,
                selectedCity,
                selectedType,
                reportId
            )

            call.enqueue(object : Callback<ServerResponse> {
                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    if (response.isSuccessful) {
                        val insertCompleted = response.body()!!

                        if (insertCompleted.status) {
                            Toast.makeText(
                                this@CreateReportActivity,
                                R.string.report_edit_success,
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@CreateReportActivity,
                                R.string.report_edit_unsuccess,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })

        }
    }

}