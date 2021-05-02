package ipvc.estg.cityhelper.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ipvc.estg.cityhelper.Login
import ipvc.estg.cityhelper.R

private lateinit var logoutBtn: Button
private lateinit var radiusInput: EditText
private lateinit var switch: Switch
private lateinit var confirm: Button

class UserAreaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_user_area, container, false)
        val sharedPref: SharedPreferences = this.activity!!.getSharedPreferences(getString(R.string.logindata), Context.MODE_PRIVATE)

        val isChecked = sharedPref.getBoolean(getString(R.string.isGeofenceActive), false)
        val radius = sharedPref.getFloat(getString(R.string.radius_meter), 0f)
        logoutBtn = root.findViewById(R.id.logout_btn)
        radiusInput = root.findViewById(R.id.radius_meters)
        switch = root.findViewById(R.id.simpleSwitch)
        confirm = root.findViewById(R.id.confirm)

        switch.isChecked = isChecked

        radiusInput.setText(radius.toString().split(".0")[0], TextView.BufferType.EDITABLE)

        logoutBtn.setOnClickListener{
            handleSharedPreferences()
            logout()
        }
        confirm.setOnClickListener{
            val sharedPref: SharedPreferences = this.activity!!.getSharedPreferences(
                getString(R.string.logindata), Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putFloat(getString(R.string.radius_meter), radiusInput.text.toString().toFloat())
                commit()
            }
            Toast.makeText(this.context!!, getString(R.string.radius_changed), Toast.LENGTH_SHORT).show()
        }

        switch.setOnClickListener{
            val sharedPref: SharedPreferences = this.activity!!.getSharedPreferences(
                getString(R.string.logindata), Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putBoolean(getString(R.string.isGeofenceActive), switch.isChecked)
                commit()
            }
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