package artground.otterbear.com.artground.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "ArtItem")
data class ArtItem(
        @PrimaryKey(autoGenerate = true)
        var _id: Long? = null,
        val title: String,
        val startDate: Date,
        val endDate: Date,
        val time: String?,
        val support: String?,
        val useFee: String?,
        val orgLink: String?,
        val mainImg: String?,
        val gcode: String?,
        val cid: Int?,
        val sponsor: String?,
        val homepage: String?,
        val etcDesc: String?,
        val inquiry: String?,
        val agelimit: String?,
        val useTarget: String?,
        val cultCode: Long?,
        val place: String?,
        val isFree: String?
)