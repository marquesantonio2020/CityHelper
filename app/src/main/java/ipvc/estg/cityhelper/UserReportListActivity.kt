package ipvc.estg.cityhelper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.ReportListAdapter
import ipvc.estg.cityhelper.api.ReportData
import ipvc.estg.cityhelper.api.ServerResponse
import ipvc.estg.cityhelper.api.endpoints.ReportEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import kotlinx.android.synthetic.main.activity_user_report_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
const val INTENT_PARAM = "fragment_report_note"

class UserReportListActivity : AppCompatActivity(), ReportListAdapter.ReportElementInterface {
    private lateinit var addReportBtn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_report_list)

        //Applies back button to Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.reports)

        fetchUsersReport()


        /**Floating button action *********/

        addReportBtn = findViewById(R.id.btn_addReport)

        addReportBtn.setOnClickListener{view ->
            val intent = Intent(this, CreateReportActivity::class.java)

            startActivity(intent)
        }

        /**********************************/
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(INTENT_PARAM, "reports")
        }
        startActivity(intent)
    }

    private fun fetchUsersReport(){
        /**Getting User Id Saved on SharedPreferences*/
        val sharedPreferences: SharedPreferences = getSharedPreferences(getString(R.string.logindata), Context.MODE_PRIVATE)

        val userId = sharedPreferences.getInt(getString(R.string.userId), -1)
        /********************************************/

        /**Preparing request to web service *********/
        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.getReportsOfUser(userId)

        call.enqueue(object : Callback<List<ReportData>>{
            override fun onResponse(call: Call<List<ReportData>>, response: Response<List<ReportData>>){
                if(response.isSuccessful){
                    report_recycler_view.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@UserReportListActivity)
                        adapter = ReportListAdapter(response.body()!!, this@UserReportListActivity)
                    }
                }
            }

            override fun onFailure(call: Call<List<ReportData>>, t: Throwable) {
                Log.v("ReportHERE", "${t.message}")
                fetchUsersReport()
            }
        })

        /********************************************/

    }

    override fun deleteReportById(id: Int) {
        Toast.makeText(this@UserReportListActivity, "${id}", Toast.LENGTH_LONG).show()
        /**Preparing request to web service - Delete issue *********/
        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.deleteReport(id)

        call.enqueue(object : Callback<ServerResponse>{
            override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>){
                if(response.isSuccessful){
                    val serverResponse = response.body()!!
                    if(serverResponse.status){
                        Toast.makeText(this@UserReportListActivity, R.string.report_deleted_success, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@UserReportListActivity, R.string.report_deleted_unseccessfully, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                Toast.makeText(this@UserReportListActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        /********************************************/
    }

    override fun editReportById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        fetchUsersReport()
    }
}


