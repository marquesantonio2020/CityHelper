package ipvc.estg.cityhelper.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ipvc.estg.cityhelper.R

class UserAreaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_area, container, false)
    }

    companion object {
        fun newInstance(): UserAreaFragment{
            return UserAreaFragment()
        }
    }
}