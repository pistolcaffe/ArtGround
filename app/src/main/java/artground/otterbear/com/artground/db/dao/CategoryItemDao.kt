package artground.otterbear.com.artground.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import artground.otterbear.com.artground.db.model.CategoryItem

@Dao
interface CategoryItemDao {
    /**
     * 모든 카테고리 불러오기
     */
    @Query("select * from CategoryItem")
    fun getAllCategories(): LiveData<MutableList<CategoryItem>>

    /**
     * 선호 카테고리만 가져오기 (대쉬보드 탭에서 사용)
     */
    @Query("select * from CategoryItem where favorite LIKE 1")
    fun getFavoriteCategories(): LiveData<MutableList<CategoryItem>>

    /**
     * 카테고리 업데이트 (선호카테고리 변경 시 사용)
     */
    @Update
    fun updateCategories(items: MutableList<CategoryItem>)
}