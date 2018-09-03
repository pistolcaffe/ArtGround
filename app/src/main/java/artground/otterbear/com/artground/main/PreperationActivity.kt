package artground.otterbear.com.artground.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.preperationfragment.PreperationFragment1
import artground.otterbear.com.artground.preperationfragment.PreperationFragment2
import kotlinx.android.synthetic.main.activity_preperation.*
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.widget.TextView
import artground.otterbear.com.artground.preperationfragment.PreperationFragment3


class PreperationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preperation)

        val vp = findViewById(R.id.preperation_vp) as ViewPager
        vp.setAdapter(pagerAdapter(supportFragmentManager))
        vp.setCurrentItem(0)

    }

    private class pagerAdapter : FragmentStatePagerAdapter {
        constructor(fm: android.support.v4.app.FragmentManager) : super(fm)

        override fun getItem(position: Int): android.support.v4.app.Fragment? {
            when (position) {
                0 -> {
                    return PreperationFragment1()
                }
                1 -> {
                    return PreperationFragment2()
                }
                2 -> {
                    return PreperationFragment3()
                }
                else -> {
                    return null
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
