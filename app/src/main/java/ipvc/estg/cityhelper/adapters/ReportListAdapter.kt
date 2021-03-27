package ipvc.estg.cityhelper.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityhelper.*
import ipvc.estg.cityhelper.api.ReportData
import kotlinx.android.synthetic.main.recycler_report_list_element.view.*
import java.io.InputStream
import java.lang.Integer.parseInt
import java.net.URL
import java.sql.Blob


const val REPORT_ID = "reportId"
//Adapter for Report List

class ReportListAdapter(val list: List<ReportData>,
                        private val noteInterf: ReportElementInterface):RecyclerView.Adapter<ReportViewHolder>(){
    interface ReportElementInterface {
        fun deleteReportById(id: Int)
        fun editReportById(id: Int)
    }

    private var reportInterface = noteInterf
    //Responsible for creating each list element of recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_report_list_element, parent, false)
        return ReportViewHolder(itemView, reportInterface)
    }

    //Indicates the recycler how many items are going to be created
    override fun getItemCount(): Int {
        return list.size
    }

    //Binds the information from an Array List to the respective list element views
    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        return holder.bind(list[position])
    }
}

//Creates references to list element views that make up the each recycler view element
class ReportViewHolder(itemView: View, reportInterface: ReportListAdapter.ReportElementInterface) : RecyclerView.ViewHolder(itemView){
    val image = itemView.report_list_image
    val title = itemView.report_list_title
    val description = itemView.report_list_description
    val location = itemView.report_list_location
    val id = itemView.hidden_report_id
    val deleteReport = itemView.btn_report_delete
    val editReport = itemView.btn_report_edit
    var reportData: ReportData? = null


    fun bind(reportData: ReportData){
        this.reportData = reportData
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        if(reportData.report.problem_picture != null){
            var imageUrl = "https://cityhelpercommov.000webhostapp.com/COMMOV_APIS/uploads/" + reportData.report.problem_picture
            var input: InputStream = URL(imageUrl).openStream()
            var myBitmap = BitmapFactory.decodeStream(input)
            image.setImageBitmap(myBitmap)
        }
        title.text = reportData.report.report_title
        description.text = reportData.report.report_description
        location.text = reportData.report.report_street + " , " + reportData.city.city_name
        id.text = reportData.report.id.toString()
    }

    init{
        itemView.setOnClickListener{ v: View ->
            //v.context returns the context of which the element is inserted into
            val intent = Intent(v.context, ReportDescriptionActivity::class.java).apply {
                putExtra(REPORT_ID, parseInt(id.text.toString()))
            }
            //Opens ReportDescription Activity
            v.context.startActivity(intent)
        }
        deleteReport.setOnClickListener{
            reportInterface.deleteReportById(parseInt(id.text.toString()))
        }
        editReport.setOnClickListener{
            val intent = Intent(it.context, CreateReportActivity::class.java)

            intent.putExtra(SELECTED_REPORT, reportData!!.report.id)
            intent.putExtra(REPORT_TITLE, reportData!!.report.report_title)
            intent.putExtra(REPORT_DESCRIPTION, reportData!!.report.report_description)
            intent.putExtra(REPORT_STREET, reportData!!.report.report_street)
            intent.putExtra(REPORT_LOCATION, reportData!!.city.id)
            intent.putExtra(REPORT_TYPE, reportData!!.type.id)

            it.context.startActivity(intent)
        }
    }


    private fun transformBlotIntoBitmap(image: Blob): Bitmap{
        val imageBytes = image.binaryStream

        val bmp: Bitmap = BitmapFactory.decodeStream(imageBytes)

        return bmp
    }
}