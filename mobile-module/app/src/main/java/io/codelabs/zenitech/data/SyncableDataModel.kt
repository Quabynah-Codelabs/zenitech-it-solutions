package io.codelabs.zenitech.data

interface SyncableDataModel : BaseDataModel {
    var synced: Boolean
    var isWishListItem: Boolean
}