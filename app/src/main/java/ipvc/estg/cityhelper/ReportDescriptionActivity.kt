package ipvc.estg.cityhelper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.REPORT_ID
import ipvc.estg.cityhelper.adapters.ReportListAdapter
import ipvc.estg.cityhelper.api.ReportData
import ipvc.estg.cityhelper.api.ServerResponse
import ipvc.estg.cityhelper.api.endpoints.ReportEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import kotlinx.android.synthetic.main.activity_user_report_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import java.io.InputStream
import java.net.URL

private lateinit var reportImage: ImageView
private lateinit var reportUser: TextView
private lateinit var reportTitle: TextView
private lateinit var reportDescription: TextView
private lateinit var reportLocation: TextView
private lateinit var reportCity: TextView
private lateinit var reportType: TextView
private lateinit var reportEdit: Button
private lateinit var reportDelete: Button
private lateinit var imgString: String
private lateinit var username: String
private var cityId: Int = 0
private var typeId: Int = 0

const val SELECTED_REPORT = "reportId"
const val REPORT_TITLE = "title"
const val REPORT_DESCRIPTION = "description"
const val REPORT_STREET = "street"
const val REPORT_LOCATION = "city"
const val REPORT_TYPE = "type"
const val REPORT_IMAGE = "image"

class ReportDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_description)

        var selectedReport = intent.getIntExtra(REPORT_ID, 0)
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.logindata), Context.MODE_PRIVATE)

        username = sharedPref.getString(getString(R.string.user), "").toString()

        fetchSingleReport(selectedReport)


    }

    private fun injectResponseData(reportData: ReportData, selectedReport: Int){
        reportImage = findViewById(R.id.report_image)
        reportUser = findViewById(R.id.report_user)
        reportTitle = findViewById(R.id.report_title)
        reportDescription = findViewById(R.id.report_description_text)
        reportLocation = findViewById(R.id.report_location)
        reportCity = findViewById(R.id.report_city)
        reportType = findViewById(R.id.report_type_problem)

        reportDelete = findViewById(R.id.report_description_delete)
        reportEdit = findViewById(R.id.report_description_edit)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        if(reportData.report.problem_picture != null){
            imgString = reportData.report.problem_picture
            var imageUrl = "https://cityhelpercommov.000webhostapp.com/COMMOV_APIS/uploads/" + reportData.report.problem_picture
            var input: InputStream = URL(imageUrl).openStream()
            var myBitmap = BitmapFactory.decodeStream(input)
            reportImage.setImageBitmap(myBitmap)
        }

        reportUser.text = "Created by: " + reportData.user
        reportTitle.text = reportData.report.report_title
        reportDescription.text = reportData.report.report_description
        reportLocation.text = reportData.report.report_street
        reportCity.text = reportData.city.city_name
        reportType.text = reportData.type.problem_description
       cityId = reportData.city.id
        typeId = reportData.type.id

        if(reportData.user != username){
            reportDelete.isVisible =  false
            reportEdit.isVisible = false
        }

        /**Delete Report from report description*/


        reportDelete.setOnClickListener{
            deleteReport(selectedReport)
            finish()


        }


        /****************************************/

        /**Edit Report - takes user to CreateReportActivity*/


        reportEdit.setOnClickListener{
            val intent = Intent(this, CreateReportActivity::class.java)

            intent.putExtra(SELECTED_REPORT, selectedReport)
            intent.putExtra(REPORT_TITLE, reportData.report.report_title)
            intent.putExtra(REPORT_DESCRIPTION, reportData.report.report_description)
            intent.putExtra(REPORT_STREET, reportData.report.report_street)
            intent.putExtra(REPORT_LOCATION, cityId)
            intent.putExtra(REPORT_TYPE, typeId)
            intent.putExtra(REPORT_IMAGE, imgString)

            startActivity(intent)

        }
        /***************************************************/

    }

    private fun fetchSingleReport(selectedReport: Int){
        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.getSingleReport(selectedReport)

        call.enqueue(object : Callback<ReportData> {
            override fun onResponse(call: Call<ReportData>, response: Response<ReportData>){
                if(response.isSuccessful){
                    injectResponseData(response.body()!!, selectedReport)
                }
            }

            override fun onFailure(call: Call<ReportData>, t: Throwable) {

                fetchSingleReport(selectedReport)
            }
        })

    }

    private fun deleteReport(selectedReport: Int){
        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.deleteReport(selectedReport)

        call.enqueue(object : Callback<ServerResponse>{
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>){
                if(response.isSuccessful){
                    val serverResponse = response.body()!!
                    if(serverResponse.status){
                        Toast.makeText(this@ReportDescriptionActivity, R.string.report_deleted_success, Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@ReportDescriptionActivity, R.string.report_deleted_unseccessfully, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                deleteReport(selectedReport)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        var selectedReport = intent.getIntExtra(REPORT_ID, 0)
        fetchSingleReport(selectedReport)
    }

}