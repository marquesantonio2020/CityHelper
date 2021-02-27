package ipvc.estg.cityhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.cityhelper.adapters.ReportLineAdapter
import ipvc.estg.cityhelper.dataclasses.Report
import kotlinx.android.synthetic.main.activity_user_report_list.*

const val INTENT_PARAM = "fragment_report_note"

class UserReportListActivity : AppCompatActivity() {
    private lateinit var reportList: ArrayList<Report>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_report_list)

        //Applies back button to Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        /** Data Injection to Recycler View*/

        //DataClass Constructor
        reportList = ArrayList<Report>()

        //Example for data injection
        for(i in 0 until 100){
            reportList.add(Report("Title $i", "Description $i", "Street Example $i, City $i"))
        }

        //Gives recycler view the created adapter
        report_recycler_view.adapter = ReportLineAdapter(reportList)
        report_recycler_view.layoutManager = LinearLayoutManager(this)

        /***********************************/
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(INTENT_PARAM, "reports")
        }
        startActivity(intent)
    }
}


