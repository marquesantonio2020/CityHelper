package ipvc.estg.cityhelper.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.ReportDescriptionActivity
import ipvc.estg.cityhelper.api.ReportData
import kotlinx.android.synthetic.main.recycler_report_list_element.view.*
import java.lang.Integer.parseInt
import java.sql.Blob
import java.util.*


const val REPORT_ID = "reportId"
//Adapter for Report List

class ReportListAdapter(val list: List<ReportData>):RecyclerView.Adapter<ReportViewHolder>(){

    //Responsible for creating each list element of recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_report_list_element, parent, false)
        return ReportViewHolder(itemView)
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
class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val image = itemView.report_list_image
    val title = itemView.report_list_title
    val description = itemView.report_list_description
    val location = itemView.report_list_location
    val id = itemView.hidden_report_id

    fun bind(reportData: ReportData){
        if(reportData.report.problem_picture != null){
            image.setImageBitmap(transformBlotIntoBitmap(reportData.report.problem_picture))
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
    }

    private fun transformBlotIntoBitmap(image: Blob): Bitmap{
        val imageBytes = image.binaryStream

        val bmp: Bitmap = BitmapFactory.decodeStream(imageBytes)

        return bmp
    }
}