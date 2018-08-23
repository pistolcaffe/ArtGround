package artground.otterbear.com.artground.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import artground.otterbear.com.artground.db.model.ArtItem
import java.util.*

/**
 * TODO : 아직 필요한 Dao API 가 결정 되지 않음
 */
@Dao
interface ArtItemDao {
    @Query("SELECT * FROM ArtItem")
    fun getAllArtItems(): MutableList<ArtItem>

    @Query("select * from ArtItem where (startDate between :from and :to) or (endDate >= :from)")
    fun findArtItemBetweenDates(from: Date, to: Date): MutableList<ArtItem>

    @Insert
    fun insert(item: ArtItem): Long

    @Insert
    fun insertAll(items: MutableList<ArtItem>)

    @Query("delete from ArtItem")
    fun deleteAll()
}