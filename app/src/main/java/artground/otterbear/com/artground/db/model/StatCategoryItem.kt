package artground.otterbear.com.artground.db.model

data class StatCategoryItem(
        val _id : Long,
        val name: String,
        val imgResName: String,
        var favorite: Boolean,
        var themeColor: String,
        var itemCount: Int
)