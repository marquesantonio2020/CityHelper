package ipvc.estg.cityhelper.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ipvc.estg.cityhelper.Login
import ipvc.estg.cityhelper.R

private lateinit var logoutBtn: Button

class UserAreaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_user_area, container, false)

        logoutBtn = root.findViewById(R.id.logout_btn)

        logoutBtn.setOnClickListener{
            handleSharedPreferences()
            logout()
        }
        // Inflate the layout for this fragment
        return root
    }

    private fun handleSharedPreferences(){
        val sharedPref: SharedPreferences = this.activity!!.getSharedPreferences(
            getString(R.string.logindata), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(getString(R.string.isLogged), false)
            commit()
        }
    }
    private fun logout(){
        val intent = Intent(this.context, Login::class.java)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): UserAreaFragment{
            return UserAreaFragment()
        }
    }
}