package ipvc.estg.cityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import ipvc.estg.cityhelper.fragments.IssueMapFragment
import ipvc.estg.cityhelper.fragments.ReportsNotesFragment
import ipvc.estg.cityhelper.fragments.UserAreaFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get intention if exists - This intention occurs when the user goes from an activity back to bottom nav bar fragments
        var intent = intent.getStringExtra(INTENT_PARAM)

        val issueFragment = IssueMapFragment()
        val reportsNotesFragment = ReportsNotesFragment()
        val profileFragment = UserAreaFragment()

        if(intent.isNullOrBlank()){
            makeCurrentFragment(issueFragment)
        }else{
            when(intent){
                "reports" -> {makeCurrentFragment(reportsNotesFragment)
                bottom_navigation.setSelectedItemId(R.id.icon_notes)
                }
                "profile" -> {makeCurrentFragment(profileFragment)
                bottom_navigation.setSelectedItemId(R.id.icon_profile)
                }
            }
        }

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.icon_map -> makeCurrentFragment(issueFragment)
                R.id.icon_notes -> makeCurrentFragment(reportsNotesFragment)
                R.id.icon_profile -> makeCurrentFragment(profileFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_wrapper, fragment)
            commit()
        }
}