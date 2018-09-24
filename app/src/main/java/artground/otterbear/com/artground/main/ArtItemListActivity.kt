package artground.otterbear.com.artground.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.Values
import artground.otterbear.com.artground.db.model.SimpleArtItem
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
import artground.otterbear.com.artground.widget.ArtItemListAdapter
import kotlinx.android.synthetic.main.activity_art_item_list.*
import kotlinx.android.synthetic.main.art_item_row.view.*

class ArtItemListActivity : AppCompatActivity() {

    private val artItemViewModel by lazy { ViewModelProviders.of(this).get(ArtItemViewModel::class.java) }
    private val artItemDataSet = mutableListOf<SimpleArtItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_item_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        artItemViewModel.getAllArtItems().observe(this, Observer { r ->
            r?.let {
                artItemDataSet.apply {
                    if (!isEmpty()) clear()
                    addAll(it)
                }
                artItemList.adapter?.notifyDataSetChanged()
            }
        })

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
