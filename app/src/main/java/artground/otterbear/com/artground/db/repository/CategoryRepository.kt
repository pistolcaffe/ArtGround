package artground.otterbear.com.artground.db.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import artground.otterbear.com.artground.db.ArtGroundDatabase
import artground.otterbear.com.artground.db.Task
import artground.otterbear.com.artground.db.dao.CategoryItemDao
import artground.otterbear.com.artground.db.model.CategoryItem
import artground.otterbear.com.artground.db.model.StatCategoryItem

class CategoryRepository(application: Application) {
    private val categoryItemDao: CategoryItemDao
    private val allCategories: LiveData<MutableList<StatCategoryItem>>
    private val favoriteCategories: LiveData<MutableList<CategoryItem>>

    init {
        val db = ArtGroundDatabase.getDatabase(application)
        categoryItemDao = db.categoryItemDao()
        allCategories = categoryItemDao.getAllCategories()
        favoriteCategories = categoryItemDao.getFavoriteCategories()
    }

    fun getAllCategories() = allCategories
    fun getRawAllCategories() = categoryItemDao.getRawAllCategories()
    fun getFavoriteCategories() = favoriteCategories
    fun updateCategories(categories: MutableList<CategoryItem>, callback: ((Void) -> Unit)?) {
        CategoryTask.UpdateAll(categoryItemDao, callback).execute()
    }
}

sealed class CategoryTask<Params, Result>(val dao: CategoryItemDao,
                                          override val callback: ((Result) -> Unit)?) : Task<Params, Result>() {

    class UpdateAll(dao: CategoryItemDao, callback: ((Void) -> Unit)?) : CategoryTask<MutableList<CategoryItem>, Void>(dao, callback) {
        override fun run(params: Array<out MutableList<CategoryItem>>): Void? {
            dao.updateCategories(params[0])
            return null
        }
    }
}