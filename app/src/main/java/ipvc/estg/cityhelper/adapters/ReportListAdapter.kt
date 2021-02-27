package ipvc.estg.cityhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.dataclasses.Report
import kotlinx.android.synthetic.main.recycler_report_list_element.view.*

//Adapter for Report List

class ReportLineAdapter(val list: ArrayList<Report>):RecyclerView.Adapter<LineViewHolder>(){

    //Responsible for creating each list element of recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_report_list_element, parent, false)
        return LineViewHolder(itemView)
    }

    //Indicates the recycler how many items are going to be created
    override fun getItemCount(): Int {
        return list.size
    }

    //Binds the information from an Array List to the respective list element views
    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentReport = list[position]

        holder.title.text = currentReport.title
        holder.description.text = currentReport.description
        holder.location.text = currentReport.street
    }
}

//Creates references to list element views that make up the each recycler view element
class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val image = itemView.report_list_image
    val title = itemView.report_list_title
    val description = itemView.report_list_description
    val location = itemView.report_list_location

    init{
        itemView.setOnClickListener{ v: View ->
            var position: Int = adapterPosition
            Toast.makeText(itemView.context, "you clicked on $position", Toast.LENGTH_LONG).show()
        }
    }
}