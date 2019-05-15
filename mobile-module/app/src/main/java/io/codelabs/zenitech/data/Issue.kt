package io.codelabs.zenitech.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "issues")
@Parcelize
data class Issue(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    var description: String,
    var category: String = Product.Category.OTHER,
    var timestamp: Long = System.currentTimeMillis()
) : BaseDataModel {

    @Ignore
    constructor() : this("", "", Product.Category.OTHER)
}