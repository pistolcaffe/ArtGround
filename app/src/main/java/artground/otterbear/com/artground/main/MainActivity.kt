package artground.otterbear.com.artground.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import kotlinx.android.synthetic.main.activity_main.*

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

    override fun onItemClick(oldPosition: Int, newPosition: Int) {
        AppLogger.LOGE("op: $oldPosition np: $newPosition")
        supportFragmentManager.beginTransaction().run {
            val oldFragment = getFragmentByPosition(oldPosition)
            val newFragment = getFragmentByPosition(newPosition)

            if (oldFragment.isAdded) hide(oldFragment)

            if (newFragment.isAdded) show(newFragment)
            else add(R.id.container, newFragment)
            commit()
        }
    }

    private fun getCurrentFragmentPosition() = (bottomNavigationFragment as BottomNavigationFragment).getCurrentItemPosition()
    private fun getFragmentByPosition(position: Int): Fragment {
        return when (position) {
            0 -> dashBoardFragment
            1 -> categoryFragment
            2 -> calendarFragment
            else -> userArtItemFragment
        }
    }
}
