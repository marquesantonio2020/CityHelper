package ipvc.estg.cityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.REPORT_ID
import ipvc.estg.cityhelper.adapters.ReportListAdapter
import ipvc.estg.cityhelper.api.ReportData
import ipvc.estg.cityhelper.api.endpoints.ReportEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import kotlinx.android.synthetic.main.activity_user_report_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body

private lateinit var reportImage: ImageView
private lateinit var reportUser: TextView
private lateinit var reportTitle: TextView
private lateinit var reportDescription: TextView
private lateinit var reportLocation: TextView
private lateinit var reportCity: TextView

class ReportDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_description)

        var selectedReport = intent.getIntExtra(REPORT_ID, 0)


        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.getSingleReport(selectedReport)

        call.enqueue(object : Callback<ReportData> {
            override fun onResponse(call: Call<ReportData>, response: Response<ReportData>){
                Toast.makeText(this@ReportDescriptionActivity, "${response}", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful){
                    injectResponseData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ReportData>, t: Throwable) {
                Log.v("ReportHERE", "${t.message}")
                Toast.makeText(this@ReportDescriptionActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun injectResponseData(reportData: ReportData){
        reportImage = findViewById(R.id.report_image)
        reportUser = findViewById(R.id.report_user)
        reportTitle = findViewById(R.id.report_title)
        reportDescription = findViewById(R.id.report_description_text)
        reportLocation = findViewById(R.id.report_location)
        reportCity = findViewById(R.id.report_city)

        if(reportData.report.problem_picture != null){
            //reportImage.setImageBitmap(reportData.report.problem_picture)
        }
        reportUser.text = "Created by: " + reportData.user
        reportTitle.text = reportData.report.report_title
        reportDescription.text = reportData.report.report_description
        reportLocation.text = reportData.city.city_street
        reportCity.text = reportData.city.city_name

    }
}