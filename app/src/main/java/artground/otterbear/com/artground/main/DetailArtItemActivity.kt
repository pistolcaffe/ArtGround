package artground.otterbear.com.artground.main

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_detail_art_item.*
import java.text.SimpleDateFormat
import java.util.*

class DetailArtItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_art_item)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val artItem = intent.extras.getSerializable(Values.EXTRA_ART_ITEM) as SimpleArtItem
        artItem.let {
            var imageInfo = it.mainImg
            imageInfo?.let { info ->
                if (info.contains(Values.API_ART_ITEM_IMAGE_URL_PREFIX)) {
                    imageInfo = StringBuilder(Values.API_ART_ITEM_IMAGE_URL_PREFIX.toLowerCase()).append(info.substring(Values.API_ART_ITEM_IMAGE_URL_PREFIX.length, info.length)).toString()
                }
            }
            GlideApp.with(this@DetailArtItemActivity).load(imageInfo).into(artItemImg)

            artItemCategory.apply {
                val categoryLayer = (background as LayerDrawable).findDrawableByLayerId(R.id.categoryBackground)
                (categoryLayer as GradientDrawable).setColor(Color.parseColor("#${it.categoryThemeColor}"))
                text = it.categoryName
            }

            val dateFormat = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
            artItemDate.text = StringBuilder().apply {
                append(dateFormat.format(it.startDate)).append("\t~\t").append(dateFormat.format(it.endDate))
            }

            artItemName.text = it.title.replace("&#39;", "\'")

            val time = it.time
            AppLogger.LOGE("time: $time")
            artItemTime.text = if (time == null || time.isBlank()) getString(R.string.no_info) else time

            val place = it.place
            artItemLocation.text = if (place == null || place == "()" || place.isBlank()) getString(R.string.no_info) else place

            val useFee = it.useFee
            artItemFee.text = if (useFee == null || useFee.isBlank()) getString(R.string.no_info) else useFee

            val webPage = it.orgLink
            artItemWebPage.text = if (webPage == null || webPage.isBlank()) getString(R.string.no_info) else webPage

            val inquiry = it.inquiry
            artItemContact.text = if (inquiry == null || inquiry.isBlank()) getString(R.string.no_info) else inquiry

            val desc = it.etcDesc
            descGroup.visibility = if (desc == null || desc.isBlank()) View.GONE else View.VISIBLE
            desc?.let { d -> if (!d.isBlank()) artItemDesc.text = d }

            reviewList.apply {
                setHasFixedSize(false)
                layoutManager = LinearLayoutManager(context.applicationContext)
                adapter = ArtItemReviewListAdapter()
            }
        }

        window.apply {
            enterTransition.duration = 200
        }
    }

    companion object {
        class ArtItemReviewListAdapter : RecyclerView.Adapter<ArtItemReviewListAdapter.ItemViewHolder>() {
            inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

            override fun getItemCount(): Int = 10
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.art_item_review_list_row, parent, false))
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                holder.itemView.apply {

                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
