package artground.otterbear.com.artground.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import artground.otterbear.com.artground.common.Values
import artground.otterbear.com.artground.db.model.DashboardReviewItem
import artground.otterbear.com.artground.db.model.SimpleArtItem
import artground.otterbear.com.artground.db.repository.DashboardCategoryFilter
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
import artground.otterbear.com.artground.widget.ReviewListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*
import kotlin.Comparator


class DashboardFragment : Fragment() {

    private val artItemViewModel by lazy { ViewModelProviders.of(this@DashboardFragment).get(ArtItemViewModel::class.java) }
    private val activeArtItems = mutableListOf<SimpleArtItem>()
    private val expectArtItems = mutableListOf<SimpleArtItem>()
    private val dataLoadCheckBitSet = BitSet(2)
    private var isCategoryRefreshing = false
    private val reviewDataSet = mutableListOf<DashboardReviewItem>()

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
                adapter = ReviewListAdapter(reviewDataSet)
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
            }, 500)
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

    /**
     * 1. LiveData 로 review Item 을 가져옴
     * 2. 총 20개를 채워야 하는데, 모자란 부분을 더미데이터로 생성
     * 3. 진행중인 데이터에서 랜덤으로 뽑아온뒤 array 에 있는 더미데이터를 가지고 reviewItem 를 생성한다
     * 4. 이때 날짜를 해당 공연 날짜 startDate ~ 현재 날짜에서 랜덤으로 뽑기
     * 5. 총 list 에서 날짜 순으로 정렬 한뒤 리스트를 갱신한다.
     */

    private val reviewItemDataObserver: Observer<MutableList<DashboardReviewItem>> = Observer { r ->
        r?.let {
            AppLogger.LOGE("size: ${it.size}\n$it")
            reviewDataSet.apply {
                if (!isEmpty()) clear()
                addAll(it)

                val remainDataSize = REVIEW_ITEM_LIMIT - size
                for (i in 0 until remainDataSize) {
                    add(makeDummyReviewItem())
                }

                this.sortWith(Comparator { o1, o2 ->
                    return@Comparator if (o1.date.time > o2.date.time) -1 else if (o1.date.time == o2.date.time) 0 else 1
                })
            }
            reviewList.adapter?.notifyDataSetChanged()
            dataLoadCheckBitSet.set(DashboardSubject.ACTIVE_ART_ITEM.index)
            checkLoadCompleteDashboardData()
        }
    }

    private fun makeDummyReviewItem(): DashboardReviewItem {
        val random = Random()
        activeArtItems[random.nextInt(activeArtItems.size)].run {
            val desc = resources.getStringArray(R.array.dummy_review).run { get(random.nextInt(size)) }
            val date = generateRandomReviewDate(startDate.time)

            return DashboardReviewItem(
                    _id = Values.DUMMY_REVIEW_ITEM_ID,
                    aid = _id,
                    desc = desc,
                    date = date,
                    title = title,
                    mainImg = mainImg,
                    categoryName = categoryName,
                    categoryThemeColor = categoryThemeColor
            )
        }
    }

    private fun generateRandomReviewDate(from: Long): Date {
        val to = System.currentTimeMillis()
        val degree = to - from
        return Date().apply { time = (from + (Random().nextDouble() * degree)).toLong() }
    }

    private val activeArtItemDataObserver: Observer<MutableList<SimpleArtItem>> = Observer { r ->
        r?.let { items ->
            activeArtItems.apply {
                if (size > 0) clear()
                addAll(items)
            }
            artItemViewPager.adapter?.notifyDataSetChanged()
            artItemViewModel.getDashboardReviewItems(getCurrentCategoryFilter()).observe(this, reviewItemDataObserver)
        }
    }

    private val expectArtItemDataObserver: Observer<MutableList<SimpleArtItem>> = Observer { r ->
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
        private const val REVIEW_ITEM_LIMIT = 20

        class DashboardArtItemPageAdapter(fm: FragmentManager,
                                          private val dataSet: MutableList<SimpleArtItem>) : FragmentStatePagerAdapter(fm) {

            override fun getItem(position: Int): Fragment {
                return DashboardArtItemFragment().apply {
                    arguments = Bundle().apply { putSerializable(Values.EXTRA_ART_ITEM, dataSet[position]) }
                }
            }

            override fun getCount() = dataSet.size
            override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

        }
    }
}

enum class DashboardSubject(val index: Int) {
    ACTIVE_ART_ITEM(0), EXPECT_ART_ITEM(1)
}