package ipvc.estg.cityhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ipvc.estg.cityhelper.fragments.IssueMapFragment
import ipvc.estg.cityhelper.fragments.ReportsNotesFragment
import ipvc.estg.cityhelper.fragments.UserAreaFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val issueFragment = IssueMapFragment()
        val reportsNotesFragment = ReportsNotesFragment()
        val profileFragment = UserAreaFragment()

        makeCurrentFragment(issueFragment)

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