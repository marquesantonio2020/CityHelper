package ipvc.estg.cityhelper.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityhelper.NoteDescriptionActivity
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.entities.Note
import kotlinx.android.synthetic.main.recycler_note_list_element.view.*

const val NOTE_ID = "noteID"

class NoteListAdapter internal constructor(
    context: Context,
    private val noteInterf: NoteElementInterface
) : RecyclerView.Adapter<NoteListAdapter.LineNoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Note>() //Cached copy of notes
    private var noteInterface = noteInterf
    //Creates references to list element views that make up the each recycler view element
    class LineNoteViewHolder(itemView: View, noteInterface: NoteElementInterface) : RecyclerView.ViewHolder(itemView){
        val title = itemView.note_list_title
        val description = itemView.note_list_description
        val deleteNoteBtn = itemView.btn_note_delete
        val editNoteBtn = itemView.btn_note_edit
        val id = itemView.note_id


        init{
            itemView.setOnClickListener{ v: View ->
                //Adapter Position return the value of the selected recycler view item
                val noteSelected: Int = Integer.parseInt(id.text.toString())
                //v.context returns the context of which the element is inserted into
                val intent = Intent(v.context, NoteDescriptionActivity::class.java).apply {
                    putExtra(NOTE_ID, noteSelected)
                }
                //Opens ReportDescription Activity
                v.context.startActivity(intent)
            }

            deleteNoteBtn.setOnClickListener{
                noteInterface.returnNoteTitle(Integer.parseInt(id.text.toString()))
            }

            editNoteBtn.setOnClickListener{
                noteInterface.editNoteById(Integer.parseInt(id.text.toString()), title.text.toString(), description.text.toString())
            }

        }

    }

    interface NoteElementInterface {
        fun returnNoteTitle(noteId: Int)
        fun editNoteById(noteId: Int, noteTitle: String, noteDescription: String)
    }

    //Responsible for creating each list element of recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineNoteViewHolder {
        val itemView = inflater.inflate(R.layout.recycler_note_list_element, parent, false)
        return LineNoteViewHolder(itemView, noteInterface)
    }

    //Indicates the recycler how many items are going to be created
    override fun getItemCount(): Int {
        return notes.size
    }

    //Binds the information from an Array List to the respective list element views
    override fun onBindViewHolder(holder: LineNoteViewHolder, position: Int) {
        val currentReport = notes[position]

        holder.title.text = currentReport.title
        holder.description.text = currentReport.description
        holder.id.text = currentReport.id.toString()
    }

    //Notifies change on note's list to the adapter
    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }



}



