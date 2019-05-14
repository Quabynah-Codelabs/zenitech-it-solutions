package io.codelabs.zenitech.data

import android.os.Parcelable

interface BaseDataModel : Parcelable {
    val key: String

    object ModelType {
        const val USER = "user"
        const val ADMIN = "admin"
        const val GUEST = "guest"
    }

}