package artground.otterbear.com.artground.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import artground.otterbear.com.artground.common.Values
import artground.otterbear.com.artground.db.model.SimpleArtItem
import artground.otterbear.com.artground.db.model.SimpleReviewItem
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
import artground.otterbear.com.artground.widget.ArtItemListAdapter
import kotlinx.android.synthetic.main.activity_art_item_list.*
import kotlinx.android.synthetic.main.art_item_row.view.*
import java.util.*

//TODO: Glide placeholder 넣기
//TODO: 각 화면에 적절한 타이틀 및 portrait 설정
//TODO: 댓글작성, 댓글 리스트, 댓글 더미데이터 운영
//TODO: 댓글 클릭 시 공연화면
//TODO: 각 공연정보 클릭 시 상세 정보 이동 intent 연결
//TODO: 공연정보 공유하기 기능 추가
//TODO: edittext next focus 설정
//TODO: 대시보드 화면 아이템이 존재하지 않을 때 빈 화면 표시
//TODO: etcDesc db 추가
//TODO: 진행중/진행예정 데이터 더보기 컨셉 정하기
/**
 * TODO 후기
 * 1. item thumbnail
 * 2. item title
 * 3. description
 * 4. pubDate
 * 5.
 *
 * 1. 모든 아이템의 리뷰 데이터를 만들기에는 너무 가지수가 많음
 * 2. 단순하게 리뷰 데이터를 만들면 진행중 / 진행예정에 맞지 않는 후기가 보임 (진행, 진행예정 데이터 구분)
 * 3. 공연내용에 어느정도 맥락이 유사한 리뷰 데이터가 필요함
 * 4. 진행예정 공연에는 리뷰를 달 수 없게 처리
 * 5. 리뷰 날짜 정하기
 *
 * 공연시작일 ~ 현재 날짜
 * 8000  10000
 *
 * 카테고
 *
 * fraction = (현재날짜 - 공연시작일) / 리뷰개수
 * (10000 - 8000) / 20 = 100
 *
 * makeDummyReviewData()
 * 해당 공연의 시작일 ~ 현재 날짜 random 뽑기 후
 * 운영중인 데이터를 뽑되 리뷰데이터를 덧붙여서 만듦
 *
 * 1. aid
 */
class ArtItemListActivity : AppCompatActivity() {

    private val artItemViewModel by lazy { ViewModelProviders.of(this).get(ArtItemViewModel::class.java) }
    private val artItemDataSet = mutableListOf<SimpleArtItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_item_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val params = intent.extras?.getSerializable(Values.EXTRA_SEARCH_PARAMS) as? SearchParams
        params?.let { p ->
            AppLogger.LOGE("p: $p")
            artItemViewModel.findArtItem(p.cid, p.start, p.end).observe(this, Observer { r ->
                r?.let {
                    AppLogger.LOGE("size: ${it.size}\n$it")
                    artItemDataSet.apply {
                        if (isEmpty()) clear()
                        addAll(it)

                        if (isEmpty()) {
                            emptySectionGroup.visibility = View.VISIBLE
                            artItemList.visibility = View.GONE
                        } else {
                            emptySectionGroup.visibility = View.GONE
                            artItemList.visibility = View.VISIBLE
                        }
                    }
                    artItemList.adapter?.notifyDataSetChanged()
                }
            })
        }

        artItemList.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context.applicationContext)
            adapter = ArtItemListAdapter(artItemDataSet).apply { setOnItemClickCallback(artItemClickCallback) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val artItemClickCallback: (Int, ArtItemListAdapter.ItemViewHolder) -> Unit = { position, holder ->
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, holder.itemView.artImg, "sharedTransition")
        startActivity(Intent(this@ArtItemListActivity, DetailArtItemActivity::class.java).apply {
            putExtra(Values.EXTRA_ART_ITEM, artItemDataSet[position])
        }, options.toBundle())
    }
}
