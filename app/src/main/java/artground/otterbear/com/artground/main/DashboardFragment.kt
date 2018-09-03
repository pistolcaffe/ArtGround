package artground.otterbear.com.artground.main

import android.arch.lifecycle.ViewModelProviders
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
import artground.otterbear.com.artground.common.Values
import artground.otterbear.com.artground.db.model.DashboardArtItem
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
import artground.otterbear.com.artground.db.viewmodel.CategoryViewModel
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

    private lateinit var artItemViewModel: ArtItemViewModel
    private val activeArtItems = mutableListOf<DashboardArtItem>()
    private val expectArtItems = mutableListOf<DashboardArtItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            artItemViewModel = ViewModelProviders.of(it).get(ArtItemViewModel::class.java)
            artItemViewModel.getDashboardActiveArtItems().observe(this, android.arch.lifecycle.Observer { r ->
                r?.let { items ->
                    activeArtItems.addAll(items)

                    artItemViewPager.apply {
                        clipToPadding = false
                        offscreenPageLimit = PAGER_OFFSET_LIMIT
                        val padding = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_pager_side_padding)
                        setPadding(padding, 0, padding, 0)
                        pageMargin = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_page_margin)
                        adapter = DashboardArtItemPageAdapter(childFragmentManager, activeArtItems)
                    }
                }
            })
            artItemViewModel.getDashboardExpectArtItems().observe(this, android.arch.lifecycle.Observer { r ->
                r?.let { items ->
                    expectArtItems.addAll(items)

                    expectArtItemViewPager.apply {
                        clipToPadding = false
                        offscreenPageLimit = PAGER_OFFSET_LIMIT
                        val padding = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_pager_side_padding)
                        setPadding(padding, 0, padding, 0)
                        pageMargin = it.resources.getDimensionPixelSize(R.dimen.dashboard_artitem_page_margin)
                        adapter = DashboardArtItemPageAdapter(childFragmentManager, expectArtItems)
                    }
                }
            })

            val cv = ViewModelProviders.of(it).get(CategoryViewModel::class.java)
            cv.getFavoriteCategories().observe(this, android.arch.lifecycle.Observer {
                AppLogger.LOGE("$it")
            })

            reviewList.apply {
                layoutManager = LinearLayoutManager(it.applicationContext)
                setHasFixedSize(true)
                adapter = ReviewAdapter()
            }

            categoryFilterChip.setOnSelectClickListener { v, selected ->
                AppLogger.LOGE("s: $selected")
                categoryFilterChip.chipText = getString(if (selected) R.string.filter_all_category else R.string.filter_favorite_category)
            }

            artItemShowAllBtn.setOnClickListener { }
        }
    }

    companion object {
        private const val PAGER_OFFSET_LIMIT = 3

        class DashboardArtItemPageAdapter(fm: FragmentManager,
                                          private val dataSet: MutableList<DashboardArtItem>) : FragmentStatePagerAdapter(fm) {
            override fun getCount(): Int {
                return dataSet.size
            }

            override fun getItem(position: Int): Fragment {
                return DashboardArtItemFragment().apply {
                    arguments = Bundle().apply { putSerializable(Values.EXTRA_ART_ITEM, dataSet[position]) }
                }
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