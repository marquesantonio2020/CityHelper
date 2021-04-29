package ipvc.estg.cityhelper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import ipvc.estg.cityhelper.api.User
import ipvc.estg.cityhelper.api.endpoints.UserEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var usernameInput: EditText
private lateinit var passwordInput: EditText
private lateinit var username: String
private lateinit var password: String
private lateinit var loginBtn: Button
private lateinit var userNotesBtn: Button
private lateinit var errorLogin: TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.logindata), Context.MODE_PRIVATE)

        val loginValue = sharedPref.getBoolean(getString(R.string.isLogged), false)


        if(loginValue){
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }

        usernameInput = findViewById(R.id.input_username)
        passwordInput = findViewById(R.id.input_password)

        loginBtn = findViewById(R.id.login_btn)
        userNotesBtn = findViewById(R.id.notes_btn)

        loginBtn.setOnClickListener{
            login()
        }

        userNotesBtn.setOnClickListener{
            val intent = Intent(this, UserNoteListActivity::class.java)
            startActivity(intent)
        }

    }

    fun login() {
        if(TextUtils.isEmpty(usernameInput.text) || TextUtils.isEmpty(passwordInput.text)){
            Toast.makeText(this, R.string.missing_fields, Toast.LENGTH_LONG).show()
        }else{
            username = usernameInput.text.toString()
            password = passwordInput.text.toString()

            val request = ServiceBuilder.buildService(UserEndPoint::class.java)
            val call = request.userLogin(username, password)

            call.enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful && response.body() != null){
                        handleSharedPreferences(response.body()!!.id, response.body()!!.username)
                        handleLoginSuccessful()
                    }else{
                        errorLogin = findViewById(R.id.login_error_message)

                        errorLogin.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    login()
                }
            })
        }
    }

    private fun handleSharedPreferences(id: Int, username: String){
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.logindata), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(getString(R.string.isLogged), true)
            putString(getString(R.string.user), username)
            putBoolean(getString(R.string.isGeofenceActive), false)
            putFloat(getString(R.string.radius_meter), 100f)
            putInt(getString(R.string.userId), id)
            commit()
        }
    }

    private fun handleLoginSuccessful(){
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
        this.finishAffinity()
    }
}