package artground.otterbear.com.artground.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.db.model.StatCategoryItem
import artground.otterbear.com.artground.db.viewmodel.CategoryViewModel
import kotlinx.android.synthetic.main.activity_favorite_category.*
import kotlinx.android.synthetic.main.favorite_category_item.view.*


class FavoriteCategory : AppCompatActivity() {

    private val categoryViewModel by lazy { ViewModelProviders.of(this).get(CategoryViewModel::class.java) }
    private val categoryDataSet = mutableListOf<StatCategoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_category)

        favorite_category.apply {
            layoutManager = GridLayoutManager(context.applicationContext,3)
            setHasFixedSize(true)
            adapter = FavoriteCategory.Companion.CategoryListAdapter(categoryDataSet)
        }

        val iter : Iterator<StatCategoryItem> = categoryDataSet.iterator()

        var checkedItemCount : Int = 0;
        fc_count.setText("12개 중 "+checkedItemCount+" 개 선택됨")


        categoryViewModel.getAllCategories().observe(this, Observer { r ->
            r?.let {
                categoryDataSet.apply {
                    if (size > 0) clear()
                    addAll(it)
                }
                favorite_category.adapter.notifyDataSetChanged()
            }
        })

        fc_btn.setOnClickListener {
            saveData()
            // DATA Set Save

            val mainItent = Intent(this, MainActivity::class.java)
            startActivity(mainItent)
            finish()
        }


    }

    private fun saveData(){

    }

    companion object {
        class CategoryListAdapter(private val dataSet: MutableList<StatCategoryItem>) : RecyclerView.Adapter<CategoryListAdapter.FCItemViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FCItemViewHolder {
                return FCItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_category_item, parent, false))
            }

            override fun getItemCount() = dataSet.size

            override fun onBindViewHolder(holder: FCItemViewHolder, position: Int) {
                holder.itemView.apply {
                    val data = dataSet[position]
                    fc_item_title.text = context.getString(R.string.category_with_count, data.name, data.itemCount)
                    fc_item_unchecked.visibility = if (data.favorite) View.GONE else View.VISIBLE
                    fc_item_checked.visibility = if (data.favorite) View.VISIBLE else View.GONE

                    val categoryBgRes = context.resources.getIdentifier(data.imgResName, "drawable", context.packageName)
                    GlideApp.with(context).load(categoryBgRes).into(fc_item_img)
                }
                holder.itemView.setOnClickListener {

                    dataSet[position].favorite = !dataSet[position].favorite
                    holder.itemView.apply {
                        fc_item_unchecked.visibility = if (dataSet[position].favorite) View.GONE else View.VISIBLE
                        fc_item_checked.visibility = if (dataSet[position].favorite) View.VISIBLE else View.GONE
                    }
                }
            }

            inner class FCItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        }
    }
}

