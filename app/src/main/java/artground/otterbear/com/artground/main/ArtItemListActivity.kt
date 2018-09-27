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
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
import artground.otterbear.com.artground.widget.ArtItemListAdapter
import kotlinx.android.synthetic.main.activity_art_item_list.*
import kotlinx.android.synthetic.main.art_item_row.view.*

//TODO: Glide placeholder 넣기
//TODO: 각 화면에 적절한 타이틀 및 portrait 설정
//TODO: 댓글작성, 댓글 리스트, 댓글 더미데이터 운영
//TODO: 댓글 클릭 시 공연화면
//TODO: 각 공연정보 클릭 시 상세 정보 이동 intent 연결
//TODO: 공연정보 공유하기 기능 추가
//TODO: edittext next focus 설정
//TODO: 대시보드 화면 아이템이 존재하지 않을 때 빈 화면 표시
/**
 * TODO 후기
 * 1. item thumbnail
 * 2. item title
 * 3. description
 * 4. pubDate
 * 5.
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
