package artground.otterbear.com.artground.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import artground.otterbear.com.artground.common.Values
import artground.otterbear.com.artground.db.model.DashboardArtItem
import artground.otterbear.com.artground.db.repository.DashboardCategoryFilter
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
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

    private val artItemViewModel by lazy { ViewModelProviders.of(this@DashboardFragment).get(ArtItemViewModel::class.java) }
    private val activeArtItems = mutableListOf<DashboardArtItem>()
    private val expectArtItems = mutableListOf<DashboardArtItem>()
    private val dataLoadCheckBitSet = BitSet(2)
    private var isCategoryRefreshing = false

    private val pagerInitOperator: ViewPager.() -> Unit = {
        clipToPadding = false
        offscreenPageLimit = PAGER_OFFSET_LIMIT
        val padding = context.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_pager_side_padding)
        setPadding(padding, 0, padding, 0)
        pageMargin = context.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_page_margin)

        val dataSet = when (id) {
            R.id.artItemViewPager -> activeArtItems
            R.id.expectArtItemViewPager -> expectArtItems
            else -> throw IllegalStateException("Unknown pager")
        }
        adapter = DashboardArtItemPageAdapter(childFragmentManager, dataSet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            artItemViewPager.run(pagerInitOperator)
            expectArtItemViewPager.run(pagerInitOperator)
            bindArtItems()

            reviewList.apply {
                layoutManager = LinearLayoutManager(it.applicationContext)
                setHasFixedSize(true)
                adapter = ReviewAdapter()
            }

            categoryFilterChip.setOnSelectClickListener { v, selected ->
                AppLogger.LOGE("s: $selected")
                if (isCategoryRefreshing) return@setOnSelectClickListener

                contentGroup.visibility = View.GONE
                dashboardLoadingView.visibility = View.VISIBLE

                categoryFilterChip.apply {
                    isEnabled = false
                    chipText = getString(if (selected) R.string.filter_all_category else R.string.filter_favorite_category)
                }
                bindArtItems()
            }

            artItemShowAllBtn.setOnClickListener { }
        }
    }

    private fun checkLoadCompleteDashboardData() {
        val okay = dataLoadCheckBitSet[0].and(dataLoadCheckBitSet[1])
        if (okay) {
            Handler().postDelayed({
                activity?.run {
                    if (isFinishing || isDestroyed) return@postDelayed
                }

                categoryFilterChip.isEnabled = true
                dataLoadCheckBitSet.clear()
                contentGroup.visibility = View.VISIBLE
                dashboardLoadingView.visibility = View.INVISIBLE
            }, 1500)
        }
        AppLogger.LOGE("okay: $okay")
    }

    private fun getCurrentCategoryFilter(): DashboardCategoryFilter {
        return if (categoryFilterChip.chipText == getString(R.string.filter_all_category)) DashboardCategoryFilter.ALL else DashboardCategoryFilter.FAVORITE
    }

    private fun bindArtItems() {
        val filter = getCurrentCategoryFilter()
        artItemViewModel.getDashboardActiveArtItems(filter).observe(this, activeArtItemDataObserver)
        artItemViewModel.getDashboardExpectArtItems(filter).observe(this, expectArtItemDataObserver)
    }

    private val activeArtItemDataObserver: Observer<MutableList<DashboardArtItem>> = Observer { r ->
        r?.let { items ->
            activeArtItems.apply {
                if (size > 0) clear()
                addAll(items)
            }
            artItemViewPager.adapter?.notifyDataSetChanged()
            dataLoadCheckBitSet.set(DashboardSubject.ACTIVE_ART_ITEM.index)
            checkLoadCompleteDashboardData()
        }
    }

    private val expectArtItemDataObserver: Observer<MutableList<DashboardArtItem>> = Observer { r ->
        r?.let { items ->
            expectArtItems.apply {
                if (size > 0) clear()
                addAll(items)
            }
            expectArtItemViewPager.adapter?.notifyDataSetChanged()
            dataLoadCheckBitSet.set(DashboardSubject.EXPECT_ART_ITEM.index)
            checkLoadCompleteDashboardData()
        }
    }

    companion object {
        private const val PAGER_OFFSET_LIMIT = 3

        class DashboardArtItemPageAdapter(fm: FragmentManager,
                                          private val dataSet: MutableList<DashboardArtItem>) : FragmentStatePagerAdapter(fm) {

            override fun getItem(position: Int): Fragment {
                return DashboardArtItemFragment().apply {
                    arguments = Bundle().apply { putSerializable(Values.EXTRA_ART_ITEM, dataSet[position]) }
                }
            }

            override fun getCount() = dataSet.size
            override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

        }
    }

    inner class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_list_row, parent, false)
            return ItemHolder(itemView)
        }

        override fun getItemCount() = 10

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.itemView.apply {
                val pos = java.util.Random().nextInt(resIds.size)
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

enum class DashboardSubject(val index: Int) {
    ACTIVE_ART_ITEM(0), EXPECT_ART_ITEM(1), REVIEW(2)
}