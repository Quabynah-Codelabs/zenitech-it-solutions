package io.codelabs.zenitech.data

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    override val key: String,
    var name: String,
    var price: Double,
    var desc: String,
    var image: String?,
    var uploadTime: Long = System.currentTimeMillis(),
    var quantity: Long = 0L,
    var category: String = Category.OTHER
) : BaseDataModel {

    constructor() : this("", "", 0.00, "", "")

    object Category {
        const val LAPTOP = "laptop"
        const val OTHER = "other"
    }
}