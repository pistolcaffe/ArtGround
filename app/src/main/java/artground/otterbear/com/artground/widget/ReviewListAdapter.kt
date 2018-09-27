package artground.otterbear.com.artground.widget

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.review_list_row.view.*

class ReviewListAdapter : RecyclerView.Adapter<ReviewListAdapter.ItemHolder>() {
    //TODO: TEST
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


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListAdapter.ItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_list_row, parent, false)
        return ItemHolder(itemView)
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: ReviewListAdapter.ItemHolder, position: Int) {
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
}