package artground.otterbear.com.artground.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import artground.otterbear.com.artground.R
import artground.otterbear.com.artground.dialog.ImagePickDialogFragment
import kotlinx.android.synthetic.main.activity_add_art_item.*
import kotlinx.android.synthetic.main.form_image_section.*
import kotlinx.android.synthetic.main.form_main_info_section.view.*
import kotlinx.android.synthetic.main.fragment_art_item_form.*

class AddArtItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_art_item)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setSectionTitleSpan(formMainInfoSection.sectionTitle, true)
        setSectionTitleSpan(formTimePlaceInfoSection.sectionTitle, true)
        setSectionTitleSpan(formAdditionalInfoSection.sectionTitle, false)
        setSectionTitleSpan(formImageSection.sectionTitle, false)

        imagePickFrame.setOnClickListener { it ->
            if (selectedImage.visibility == View.VISIBLE) {
                ImagePickDialogFragment().apply {
                    setOnItemClickListener { pos ->
                        if (pos == 0) {
                            doActionImagePick()
                        } else {
                            this@AddArtItemActivity.selectedImage.setImageDrawable(null)
                            this@AddArtItemActivity.selectedImage.visibility = View.GONE
                            this@AddArtItemActivity.emptySectionGroup.visibility = View.VISIBLE
                        }
                    }
                    show(supportFragmentManager, "image_pick")
                }

            } else {
                doActionImagePick()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK) {
            data?.let { intent ->
                selectedImage.visibility = View.VISIBLE
                emptySectionGroup.visibility = View.GONE

                val info = intent.data
                GlideApp.with(this).load(info).into(selectedImage)
            }
        }
    }

    private fun doActionImagePick() {
        Intent(Intent.ACTION_PICK).apply {
            type = MediaStore.Images.Media.CONTENT_TYPE
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(this, REQUEST_IMAGE_PICK)
        }
    }

    private fun setSectionTitleSpan(titleView: TextView, isEssential: Boolean) {
        val spanColor = if (isEssential) ContextCompat.getColor(this, R.color.colorPrimary) else Color.DKGRAY
        val sb = SpannableStringBuilder(titleView.text).apply {
            setSpan(ForegroundColorSpan(spanColor), lastIndex - 4, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        titleView.text = sb
    }

    companion object {
        const val REQUEST_IMAGE_PICK = 100

        class ArtItemFormFragmentPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
            override fun getCount() = 3
            override fun getItem(position: Int): Fragment {
                return ArtItemFormFragment().apply {
                    arguments = Bundle().apply { putInt("idx", position + 1) }
                }
            }
        }

        class ArtItemFormFragment : Fragment() {
            override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                return inflater.inflate(R.layout.fragment_art_item_form, container, false)
            }

            override fun onActivityCreated(savedInstanceState: Bundle?) {
                super.onActivityCreated(savedInstanceState)
                arguments?.getInt("idx")?.let {
                    if (it == 0) {
                        idx.visibility = View.GONE
                    }
                    idx.text = it.toString()
                }
            }
        }
    }
}
