package io.codelabs.zenitech.core.util

import android.widget.EditText
import io.codelabs.zenitech.data.SyncableDataModel

fun EditText.isNotEmpty(): Boolean = this.text.isNotEmpty()

fun MutableList<out SyncableDataModel>.markAsSynced(synced: Boolean = true) = this.apply {
    forEach {
        it.synced = synced
    }
}

