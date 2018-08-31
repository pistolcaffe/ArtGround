package artground.otterbear.com.artground.main

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.review_list_row.view.*
import java.util.*

class DashboardFragment : Fragment() {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            artItemViewPager.apply {
                clipToPadding = false
                val padding = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_pager_side_padding)
                setPadding(padding, 0, padding, 0)
                pageMargin = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_page_margin)
                adapter = DashboardArtItemPageAdapter(childFragmentManager)
            }

            expectArtItemViewPager.apply {
                clipToPadding = false
                val padding = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_pager_side_padding)
                setPadding(padding, 0, padding, 0)
                pageMargin = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_page_margin)
                adapter = DashboardArtItemPageAdapter(childFragmentManager)
            }

            reviewList.apply {
                layoutManager = LinearLayoutManager(it.applicationContext)
                setHasFixedSize(true)
                adapter = ReviewAdapter()
            }

            categoryFilterChip.setOnSelectClickListener { v, selected ->
                AppLogger.LOGE("s: $selected")
                categoryFilterChip.chipText = getString(if (selected) R.string.filter_all_category else R.string.filter_favorite_category)
            }
        }
    }

    inner class DashboardArtItemPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return 13
        }

        override fun getItem(position: Int): Fragment {
            return DashboardArtItemFragment().apply {
                val b = Bundle()
                b.putInt("position", position)
                arguments = b
            }
        }
    }

    inner class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_list_row, parent, false)
            return ItemHolder(itemView)
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.itemView.apply {
                val pos = Random().nextInt(resIds.size)
                Glide.with(context).load(resIds[pos]).into(reviewImg)

                artItemCategory.apply {
                    val d = (background as LayerDrawable).findDrawableByLayerId(R.id.categoryBackground)
                    AppLogger.LOGE("d: $d")
                    (d as GradientDrawable).setColor(Color.parseColor("#f57f17"))
                    text = "문화강좌/교양"
                }

                reviewDesc.text = if (position % 2 == 0) "정말 재미있어요."
                else {
                    "정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요." +
                            "정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요." +
                            "정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요.정말 재미있어요."
                }
            }
        }

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}