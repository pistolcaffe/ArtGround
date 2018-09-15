package artground.otterbear.com.artground.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import artground.otterbear.com.artground.common.Values
import artground.otterbear.com.artground.db.model.SimpleArtItem
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import kotlinx.android.synthetic.main.activity_art_item_list.*
import kotlinx.android.synthetic.main.art_item_row.view.*
import java.text.SimpleDateFormat
import java.util.*

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

    companion object {
        class ArtItemListAdapter(private val dataSet: MutableList<SimpleArtItem>) : RecyclerView.Adapter<ArtItemListAdapter.ItemViewHolder>() {
            private val dateFormat = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
            private var itemClickCallback: ((Int, ItemViewHolder) -> Unit)? = null

            inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

            fun setOnItemClickCallback(callback: (Int, ItemViewHolder) -> Unit) {
                itemClickCallback = callback
            }

            override fun getItemCount() = dataSet.size
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.art_item_row, parent, false))
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                holder.itemView.apply {
                    var imageInfo = dataSet[position].mainImg
                    imageInfo?.let { info ->
                        if (info.contains(Values.API_ART_ITEM_IMAGE_URL_PREFIX)) {
                            imageInfo = StringBuilder(Values.API_ART_ITEM_IMAGE_URL_PREFIX.toLowerCase()).append(info.substring(Values.API_ART_ITEM_IMAGE_URL_PREFIX.length, info.length)).toString()
                        }
                    }

                    GlideApp.with(context).load(imageInfo).transition(withCrossFade()).into(artImg)
                    artItemName.text = dataSet[position].title.replace("&#39;", "\'")

                    artItemDate.text = StringBuilder().apply {
                        append(dateFormat.format(dataSet[position].startDate)).append("\t~\t").append(dateFormat.format(dataSet[position].endDate))
                    }

                    val place = dataSet[position].place

                    artItemLocation.text = if (place == null || place == "()") context.getString(R.string.no_info) else place.trim()
                    officialDataBadge.visibility = if (dataSet[position].cultCode != null) View.VISIBLE else View.GONE
                    AppLogger.LOGE("p: ${artItemName.text} l: ${artItemName.length()}")

                    artItemCategory.apply {
                        val categoryLayer = (background as LayerDrawable).findDrawableByLayerId(R.id.categoryBackground)
                        (categoryLayer as GradientDrawable).setColor(Color.parseColor("#${dataSet[position].categoryThemeColor}"))
                        text = dataSet[position].categoryName
                    }

                    rowParent.setOnClickListener { itemClickCallback?.invoke(position, holder) }
                }
            }
        }
    }
}
