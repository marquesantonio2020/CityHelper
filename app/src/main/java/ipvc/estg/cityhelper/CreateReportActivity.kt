package ipvc.estg.cityhelper

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.ReportListAdapter
import ipvc.estg.cityhelper.api.*
import ipvc.estg.cityhelper.api.endpoints.CountryCityEndpoint
import ipvc.estg.cityhelper.api.endpoints.ReportEndPoint
import ipvc.estg.cityhelper.api.endpoints.TypeEndPoint
import ipvc.estg.cityhelper.api.endpoints.UserEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import kotlinx.android.synthetic.main.activity_user_report_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body

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


        /**Preparing request to web service - Getting all Cities available *********/
        val request = ServiceBuilder.buildService(CountryCityEndpoint::class.java)
        val call = request.getAllCities()

        call.enqueue(object : Callback<List<CountryCity>> {
            override fun onResponse(call: Call<List<CountryCity>>, response: Response<List<CountryCity>>){
                if(response.isSuccessful){
                    cityResponse = response.body()!!
                    inflateDataIntoSpinnerCities(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<CountryCity>>, t: Throwable) {
                Log.v("ReportHERE", "${t.message}")
                Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        /********************************************/

        /**Preparing request to web service - Getting all Types of problems available *********/
        val requestTypes = ServiceBuilder.buildService(TypeEndPoint::class.java)
        val callTypes = requestTypes.getTypesProblems()

        callTypes.enqueue(object : Callback<List<Type>> {
            override fun onResponse(call: Call<List<Type>>, response: Response<List<Type>>){
                if(response.isSuccessful){
                    typeResponse = response.body()!!
                    inflateDataIntoSpinnerTypes(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Type>>, t: Throwable) {
                Log.v("ReportHERE", "${t.message}")
                Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        /********************************************/

        /**Report Creation Method **********************/
        createReport = findViewById(R.id.report_create_btn)

        createReport.setOnClickListener{
            createReport()
        }
        /***********************************************/

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

    private fun createReport(){
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

        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.newReport(title, description, 2.2222, 2.2222, local, userID, selectedCity, selectedType)

        call.enqueue(object : Callback<InsertServerResponse>{
            override fun onResponse(call: Call<InsertServerResponse>, response: Response<InsertServerResponse>) {
                if(response.isSuccessful){
                    val insertCompleted = response.body()!!

                    if(insertCompleted.status){
                        Toast.makeText(this@CreateReportActivity, "sucesso", Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@CreateReportActivity, "erro", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<InsertServerResponse>, t: Throwable) {
                Toast.makeText(this@CreateReportActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}