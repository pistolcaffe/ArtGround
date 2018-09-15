package artground.otterbear.com.artground.db.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import artground.otterbear.com.artground.db.model.SimpleArtItem
import artground.otterbear.com.artground.db.repository.ArtItemRepository
import artground.otterbear.com.artground.db.repository.DashboardCategoryFilter

class ArtItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArtItemRepository = ArtItemRepository(application)
    private lateinit var dashboardActiveArtItems: LiveData<MutableList<SimpleArtItem>>
    private lateinit var dashboardExpectArtItems: LiveData<MutableList<SimpleArtItem>>

    fun getAllArtItems() = repository.getAllArtItems()

    fun getDashboardActiveArtItems(filter: DashboardCategoryFilter): LiveData<MutableList<SimpleArtItem>> {
        dashboardActiveArtItems = repository.getDashboardActiveArtItems(filter)
        return dashboardActiveArtItems
    }

    fun getDashboardExpectArtItems(filter: DashboardCategoryFilter): LiveData<MutableList<SimpleArtItem>> {
        dashboardExpectArtItems = repository.getDashboardExpectArtItems(filter)
        return dashboardExpectArtItems
    }
}