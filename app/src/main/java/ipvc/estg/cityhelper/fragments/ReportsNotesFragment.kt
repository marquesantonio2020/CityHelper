package ipvc.estg.cityhelper.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.UserNoteListActivity
import ipvc.estg.cityhelper.UserReportListActivity

private lateinit var toReportBtn : ImageButton
private lateinit var toNoteBtn : ImageButton


class ReportsNotesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_reports_notes, container, false)

        toReportBtn = root.findViewById(R.id.button_goto_reports)

        toReportBtn.setOnClickListener{
            val intent = Intent(this.context, UserReportListActivity::class.java)

            startActivity(intent)
        }

        toNoteBtn = root.findViewById(R.id.button_goto_notes)

        toNoteBtn.setOnClickListener{
            val intent = Intent(this.context, UserNoteListActivity::class.java)

            startActivity(intent)
        }

        return root
    }

    companion object {
        fun newInstance(): ReportsNotesFragment{
            return ReportsNotesFragment()
        }
    }
}