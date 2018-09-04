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

    init {
        val db = ArtGroundDatabase.getDatabase(application)
        artItemDao = db.artItemDao()
        val current = Date(System.currentTimeMillis())
        AppLogger.LOGE("current: $current currentMillis: ${current.time}")
    }

    fun getDashboardActiveArtItems(filter: DashboardCategoryFilter): LiveData<MutableList<DashboardArtItem>> {
        val current = Date(System.currentTimeMillis())
        return when (filter) {
            DashboardCategoryFilter.ALL -> artItemDao.getActiveArtItemsOnAllCategory(current)
            DashboardCategoryFilter.FAVORITE -> artItemDao.getActiveArtItemsOnFavoriteCategory(current)
        }
    }

    fun getDashboardExpectArtItems(filter: DashboardCategoryFilter): LiveData<MutableList<DashboardArtItem>> {
        val current = Date(System.currentTimeMillis())
        return when (filter) {
            DashboardCategoryFilter.ALL -> artItemDao.getExpectArtItemsOnAllCategory(current)
            DashboardCategoryFilter.FAVORITE -> artItemDao.getExpectArtItemsOnFavoriteCategory(current)
        }
    }
}

enum class DashboardCategoryFilter {
    FAVORITE, ALL
}