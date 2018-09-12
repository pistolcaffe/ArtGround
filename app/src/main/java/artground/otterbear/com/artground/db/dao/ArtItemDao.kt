package artground.otterbear.com.artground.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import artground.otterbear.com.artground.db.model.ArtItem
import artground.otterbear.com.artground.db.model.DashboardArtItem
import java.util.*

/**
 * TODO : 아직 필요한 Dao API 가 결정 되지 않음
 */
@Dao
interface ArtItemDao {
    @Query("select * from ArtItem limit 20")
    fun getAllArtItems(): LiveData<MutableList<ArtItem>>

    @Query("select * from ArtItem where (startDate between :from and :to) or (endDate >= :from)")
    fun findArtItemBetweenDates(from: Date, to: Date): MutableList<ArtItem>

//    @Query("select a._id, a.title, a.cultCode, a.startDate, a.endDate, a.mainImg, a.place, b.name, b.themeColor from ArtItem as a join CategoryItem as b on a.cid = b._id where :current between startDate and endDate order by endDate asc limit :count")
//    fun getActiveArtItems(current: Date, count: Int): LiveData<MutableList<DashboardArtItem>>

//    @Query("select ArtItem._id, ArtItem.title, ArtItem.cultCode, ArtItem.startDate, ArtItem.endDate, ArtItem.mainImg, ArtItem.place, CategoryItem.name, CategoryItem.themeColor from ArtItem inner join CategoryItem on ArtItem.cid = CategoryItem._id where startDate > :current order by startDate asc limit :count")
//    fun getExpectArtItems(current: Date, count: Int): LiveData<MutableList<DashboardArtItem>>

    @Query("select a._id, a.title, a.cultCode, a.startDate, a.endDate, a.mainImg, a.place, b.name, b.themeColor from ArtItem as a join CategoryItem as b on a.cid = b._id where :current between startDate and endDate order by endDate asc limit 15")
    fun getActiveArtItemsOnAllCategory(current: Date): LiveData<MutableList<DashboardArtItem>>

    @Query("select _id, title, startDate, endDate, place, cultCode, mainImg, name, themeColor from (select _id, cid, title, startDate, endDate, place, cultCode, mainImg from ArtItem as a where _id in (select b._id from ArtItem as b where a.cid = b.cid and :current between startDate and endDate limit 5)) as art join (select _id as _cid, name, themeColor from CategoryItem where favorite = 1) as category on art.cid = category._cid order by endDate asc")
    fun getActiveArtItemsOnFavoriteCategory(current: Date): LiveData<MutableList<DashboardArtItem>>

    @Query("select a._id, a.title, a.cultCode, a.startDate, a.endDate, a.mainImg, a.place, b.name, b.themeColor from ArtItem as a join CategoryItem as b on a.cid = b._id where startDate > :current order by startDate asc limit 15")
    fun getExpectArtItemsOnAllCategory(current: Date): LiveData<MutableList<DashboardArtItem>>

    @Query("select _id, title, startDate, endDate, place, cultCode, mainImg, name, themeColor from (select _id, cid, title, startDate, endDate, place, cultCode, mainImg from ArtItem as a where _id in (select b._id from ArtItem as b where a.cid = b.cid and startDate > :current limit 5)) as art join (select _id as _cid, name, themeColor from CategoryItem where favorite = 1) as category on art.cid = category._cid order by startDate asc")
    fun getExpectArtItemsOnFavoriteCategory(current: Date): LiveData<MutableList<DashboardArtItem>>

    @Insert
    fun insert(item: ArtItem): Long

    @Insert
    fun insertAll(items: MutableList<ArtItem>)

    @Query("delete from ArtItem")
    fun deleteAll()
}