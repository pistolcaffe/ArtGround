package artground.otterbear.com.artground.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.common.Values
import artground.otterbear.com.artground.db.model.SimpleArtItem
import artground.otterbear.com.artground.db.viewmodel.ArtItemViewModel
import artground.otterbear.com.artground.widget.ArtItemListAdapter
import kotlinx.android.synthetic.main.art_item_row.view.*
import kotlinx.android.synthetic.main.fragment_user_artitem.*

class UserArtItemFragment : Fragment() {

    private val artItemViewModel by lazy { ViewModelProviders.of(this).get(ArtItemViewModel::class.java) }
    private val artItemDataSet = mutableListOf<SimpleArtItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_artitem, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        artItemViewModel.getAllArtItems().observe(this, Observer { r ->
            r?.let {
                artItemDataSet.apply {
                    if (!isEmpty()) clear()
                    addAll(it)
                }
                userArtItemList.adapter?.notifyDataSetChanged()
            }
        })

        userArtItemList.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context.applicationContext)
            adapter = ArtItemListAdapter(artItemDataSet).apply { setOnItemClickCallback(artItemClickCallback) }
        }

        artItemWriteBtn.setOnClickListener {
            startActivity(Intent(it.context, AddArtItemActivity::class.java))
        }
    }

    private val artItemClickCallback: (Int, ArtItemListAdapter.ItemViewHolder) -> Unit = { position, holder ->
        activity?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it, holder.itemView.artImg, "sharedTransition")
            startActivity(Intent(it, DetailArtItemActivity::class.java).apply {
                putExtra(Values.EXTRA_ART_ITEM, artItemDataSet[position])
            }, options.toBundle())
        }
    }
}