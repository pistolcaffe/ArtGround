package artground.otterbear.com.artground.db.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import artground.otterbear.com.artground.db.model.CategoryItem
import artground.otterbear.com.artground.db.model.StatCategoryItem
import artground.otterbear.com.artground.db.repository.CategoryRepository

/**
 * use [ViewModelProviders.of(activity or fragment).get(CategoryViewModel::class.java)] to get instance
 */
class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CategoryRepository(application)
    private val allCategories: LiveData<MutableList<StatCategoryItem>>
    private val favoriteCategories: LiveData<MutableList<CategoryItem>>

    init {
        allCategories = repository.getAllCategories()
        favoriteCategories = repository.getFavoriteCategories()
    }

    fun getAllCategories() = allCategories
    fun getFavoriteCategories() = favoriteCategories
    fun updateCategories(categories: MutableList<CategoryItem>, listener: ((Void) -> Unit)? = null) {
        repository.updateCategories(categories, listener)
    }
}