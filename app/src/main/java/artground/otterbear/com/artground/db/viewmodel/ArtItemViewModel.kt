package artground.otterbear.com.artground.db.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import artground.otterbear.com.artground.db.model.DashboardArtItem
import artground.otterbear.com.artground.db.repository.ArtItemRepository

class ArtItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArtItemRepository = ArtItemRepository(application)
    private val dashboardActiveArtItems: LiveData<MutableList<DashboardArtItem>>
    private val dashboardExpectArtItems: LiveData<MutableList<DashboardArtItem>>

    init {
        dashboardActiveArtItems = repository.getDashboardActiveArtItems()
        dashboardExpectArtItems = repository.getDashboardExpectArtItems()
    }

    fun getDashboardActiveArtItems() = dashboardActiveArtItems
    fun getDashboardExpectArtItems() = dashboardExpectArtItems
}