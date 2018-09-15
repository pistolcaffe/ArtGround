package artground.otterbear.com.artground.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.AppLogger
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.simple_category_list_item.view.*

class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val strings = arrayOf(
                "모든 카테고리",
                "콘서트",
                "뮤지컬/오페라",
                "연극",
                "국악",
                "영화",
                "기타",
                "길거리 공연",
                "무용",
                "문화교양/강좌",
                "독주/독창회",
                "전시회",
                "축제",
                "클래식")
        val adapter = ArrayAdapter<String>(context!!, R.layout.category_spinner_item, 0, strings)
        adapter.setDropDownViewResource(R.layout.category_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        favoriteCategoryList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context.applicationContext, 3)
            this.adapter = SimpleCategoryListAdapter()
            setOnClickListener {
                AppLogger.stamp()
            }
        }

        //TODO : 원복
        searchBtn.isEnabled = true

        startDateBtn.setDateInfoListener {
            AppLogger.LOGE("startDate: $it")
            endDateBtn.getDateInfo()?.run {
                if (it.time > this.time) {
                    Toast.makeText(startDateBtn.context, R.string.start_date_error_msg_1, Toast.LENGTH_SHORT).show()
                    return@setDateInfoListener false
                }
                searchBtn.isEnabled = true
            }
            return@setDateInfoListener true
        }

        endDateBtn.setDateInfoListener {
            AppLogger.LOGE("endDate: $it")
            startDateBtn.getDateInfo()?.run {
                if (it.time < this.time) {
                    Toast.makeText(startDateBtn.context, R.string.end_date_error_msg_1, Toast.LENGTH_SHORT).show()
                    return@setDateInfoListener false
                }
                searchBtn.isEnabled = true
            }

            return@setDateInfoListener true
        }

        searchBtn.setOnClickListener {
            Intent(it.context, ArtItemListActivity::class.java).run{
                startActivity(this)
            }
        }
    }

    companion object {
        class SimpleCategoryListAdapter : RecyclerView.Adapter<SimpleCategoryListAdapter.ItemViewHolder>() {
            inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                init {
                    itemView.setOnClickListener {
                        AppLogger.stamp()
                    }
                }
            }

            override fun getItemCount() = 3
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_category_list_item, parent, false))
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                holder.itemView.apply {
                    when (position) {
                        0 -> {
                            GlideApp.with(context).load(R.drawable.category_bg_concert).into(categoryImg)
                            categoryName.text = "콘서트"
                        }
                        1 -> {
                            GlideApp.with(context).load(R.drawable.category_bg_musical_opera).into(categoryImg)
                            categoryName.text = "뮤지컬/오페라"
                        }
                        else -> {
                            GlideApp.with(context).load(R.drawable.category_bg_busking).into(categoryImg)
                            categoryName.text = "길거리 공연"
                        }
                    }
                }
            }
        }
    }
}