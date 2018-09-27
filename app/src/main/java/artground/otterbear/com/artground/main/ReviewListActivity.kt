package artground.otterbear.com.artground.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.widget.ReviewListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

class ReviewListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        reviewList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            adapter = ReviewListAdapter()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
