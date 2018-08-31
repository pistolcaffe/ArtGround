package artground.otterbear.com.artground.main

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_dashboard_artitem.*
import java.util.*

class DashboardArtItemFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard_artitem, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val p = arguments!!.getInt("position")

        //TODO : Test
        val resIds = arrayOf(R.drawable.category_bg_busking,
                R.drawable.category_bg_classic,
                R.drawable.category_bg_concert,
                R.drawable.category_bg_culture_lecture,
                R.drawable.category_bg_dance,
                R.drawable.category_bg_etc,
                R.drawable.category_bg_exhibition,
                R.drawable.category_bg_festival,
                R.drawable.category_bg_gukak,
                R.drawable.category_bg_movie,
                R.drawable.category_bg_musical_opera,
                R.drawable.category_bg_solo,
                R.drawable.category_bg_theater)

        val pos = Random().nextInt(resIds.size)

        Glide.with(this).load(resIds[pos]).into(artItemImg)
        artItemCategory.apply {
            val d = (background as LayerDrawable).findDrawableByLayerId(R.id.categoryBackground)
            AppLogger.LOGE("d: $d")
            //(d as GradientDrawable).setColor(Color.parseColor("#f57f17"))
            (d as GradientDrawable).setColor(Color.parseColor(getColorString(p)))
            //setBackgroundColor(Color.parseColor("#f57f17"))
            text = "길거리 공연"
        }
    }

    //TODO : Test
    private fun getColorString(position: Int): String {
        return when (position) {
            0 -> "#f57f17"
            1 -> "#1b5e20"
            2 -> "#006064"
            3 -> "#004D40"
            4 -> "#880E4F"
            5 -> "#4A148C"
            6 -> "#B71C1C"
            7 -> "#827717"
            8 -> "#3E2723"
            9 -> "#BF360C"
            10 -> "#263238"
            11 -> "#0D47A1"
            else -> "#880e4f"
        }
    }
}