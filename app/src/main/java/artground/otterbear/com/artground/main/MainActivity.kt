package artground.otterbear.com.artground.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger

class MainActivity : AppCompatActivity(), OnNavigationItemClickListener {

    private val dashBoardFragment by lazy { DashboardFragment() }
    private val categoryFragment by lazy { CategoryFragment() }
    private val calendarFragment by lazy { CalendarFragment() }
    private val userArtItemFragment by lazy { UserArtItemFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.container, dashBoardFragment).commit()
    }

    override fun onItemClick(position: Int) {
        AppLogger.LOGE("position: $position")
        val ft = supportFragmentManager.beginTransaction()
        val fragment = when (position) {
            0 -> dashBoardFragment
            1 -> categoryFragment
            2 -> calendarFragment
            else -> userArtItemFragment
        }
        ft.replace(R.id.container, fragment).commit()
    }
}
