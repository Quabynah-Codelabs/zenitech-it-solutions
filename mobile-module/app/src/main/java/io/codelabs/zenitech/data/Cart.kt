package io.codelabs.zenitech.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "carts")
data class Cart(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    val user: String?,
    val product: String?,
    var createdAt: Long = System.currentTimeMillis()
) : BaseDataModel {
    @Ignore
    constructor() : this("", "", "")
}