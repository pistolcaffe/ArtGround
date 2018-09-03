package artground.otterbear.com.artground.db.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import artground.otterbear.com.artground.common.AppLogger
import artground.otterbear.com.artground.db.ArtGroundDatabase
import artground.otterbear.com.artground.db.dao.ArtItemDao
import artground.otterbear.com.artground.db.model.DashboardArtItem
import java.util.*

class ArtItemRepository(application: Application) {
    private val artItemDao: ArtItemDao
    private val dashboardActiveArtItems: LiveData<MutableList<DashboardArtItem>>
    private val dashboardExpectArtItems: LiveData<MutableList<DashboardArtItem>>

    init {
        val db = ArtGroundDatabase.getDatabase(application)
        artItemDao = db.artItemDao()
        val current = Date(System.currentTimeMillis())
        AppLogger.LOGE("current: $current currentMillis: ${current.time}")
        dashboardActiveArtItems = artItemDao.getActiveArtItems(current, 15)
        dashboardExpectArtItems = artItemDao.getExpectArtItems(current, 15)
    }

    fun getDashboardActiveArtItems() = dashboardActiveArtItems
    fun getDashboardExpectArtItems() = dashboardExpectArtItems
}