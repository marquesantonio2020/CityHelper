package ipvc.estg.cityhelper.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityhelper.NoteDescriptionActivity
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.ReportDescriptionActivity
import ipvc.estg.cityhelper.dataclasses.Note
import kotlinx.android.synthetic.main.recycler_note_list_element.view.*

class NoteListAdapter (val list: ArrayList<Note>):RecyclerView.Adapter<LineNoteViewHolder>(){
    //Responsible for creating each list element of recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineNoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_note_list_element, parent, false)
        return LineNoteViewHolder(itemView)
    }

    //Indicates the recycler how many items are going to be created
    override fun getItemCount(): Int {
        return list.size
    }

    //Binds the information from an Array List to the respective list element views
    override fun onBindViewHolder(holder: LineNoteViewHolder, position: Int) {
        val currentReport = list[position]

        holder.title.text = currentReport.title
        holder.description.text = currentReport.description
    }
}

//Creates references to list element views that make up the each recycler view element
class LineNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val title = itemView.note_list_title
    val description = itemView.note_list_description

    init{
        itemView.setOnClickListener{ v: View ->
            //Adapter Positon return the value of the selected recycler view item
            var position: Int = adapterPosition
            //v.context returns the context of which the element is inserted into
            val intent = Intent(v.context, NoteDescriptionActivity::class.java).apply {
                putExtra("reportId", position)
            }
            //Opens ReportDescription Activity
            v.context.startActivity(intent)
        }
    }
}