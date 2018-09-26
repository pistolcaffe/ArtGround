package artground.otterbear.com.artground.db.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import artground.otterbear.com.artground.common.AppLogger
import artground.otterbear.com.artground.db.ArtGroundDatabase
import artground.otterbear.com.artground.db.Task
import artground.otterbear.com.artground.db.dao.ArtItemDao
import artground.otterbear.com.artground.db.model.ArtItem
import artground.otterbear.com.artground.db.model.SimpleArtItem
import artground.otterbear.com.artground.db.model.UserArtItem
import java.util.*

class ArtItemRepository(application: Application) {
    private val artItemDao: ArtItemDao

    init {
        val db = ArtGroundDatabase.getDatabase(application)
        artItemDao = db.artItemDao()
        val current = Date(System.currentTimeMillis())
        AppLogger.LOGE("current: $current currentMillis: ${current.time}")
    }

    fun getAllArtItems() = artItemDao.getAllArtItems()
    fun getAllUserArtItems() = artItemDao.getAllUserArtItem()

    fun getDashboardActiveArtItems(filter: DashboardCategoryFilter): LiveData<MutableList<SimpleArtItem>> {
        val current = Date(System.currentTimeMillis())
        return when (filter) {
            DashboardCategoryFilter.ALL -> artItemDao.getActiveArtItemsOnAllCategory(current)
            DashboardCategoryFilter.FAVORITE -> artItemDao.getActiveArtItemsOnFavoriteCategory(current)
        }
    }

    fun getDashboardExpectArtItems(filter: DashboardCategoryFilter): LiveData<MutableList<SimpleArtItem>> {
        val current = Date(System.currentTimeMillis())
        return when (filter) {
            DashboardCategoryFilter.ALL -> artItemDao.getExpectArtItemsOnAllCategory(current)
            DashboardCategoryFilter.FAVORITE -> artItemDao.getExpectArtItemsOnFavoriteCategory(current)
        }
    }

    fun insertArtItem(artItem: ArtItem, listener: ((Long) -> Unit)? = null) {
        ArtItemTask.Insert(artItemDao, listener).execute(artItem)
    }

    fun deleteArtItem(aid: Long) {
        ArtItemTask.Delete(artItemDao).execute(aid)
    }

    fun updateArtItem(artItem: ArtItem, listener: ((Int) -> Unit)? = null) {
        ArtItemTask.Update(artItemDao, listener).execute(artItem)
    }
}

sealed class ArtItemTask<Params, Result>(val artItemDao: ArtItemDao,
                                         override val callback: ((Result) -> Unit)?) : Task<Params, Result>() {
    class Insert(dao: ArtItemDao, callback: ((Long) -> Unit)?) : ArtItemTask<ArtItem, Long>(dao, callback) {
        override fun run(params: Array<out ArtItem>): Long {
            val aid = artItemDao.insert(params[0])
            return artItemDao.insertUserArtItem(UserArtItem(aid = aid))
        }
    }

    class Delete(dao: ArtItemDao) : ArtItemTask<Long, Void>(dao, callback = null) {
        override fun run(params: Array<out Long>): Void? {
            artItemDao.delete(params[0])
            return null
        }
    }

    class Update(dao: ArtItemDao, callback: ((Int) -> Unit)?) : ArtItemTask<ArtItem, Int>(dao, callback) {
        override fun run(params: Array<out ArtItem>): Int {
            return artItemDao.update(params[0])
        }
    }
}

enum class DashboardCategoryFilter {
    FAVORITE, ALL
}