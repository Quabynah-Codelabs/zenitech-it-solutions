package io.codelabs.zenitech.core.util

import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import io.codelabs.zenitech.data.SyncableDataModel

// Checks if a TextView field is not emapty
fun TextView.isNotEmpty(): Boolean = this.text.isNotEmpty()

// Checks if a text field is not numeric
fun TextView.isNotNumeric(): Boolean = !TextUtils.isDigitsOnly(this.text.toString())

// Marks all syncable objects as synced or not
fun MutableList<out SyncableDataModel>.markAsSynced(synced: Boolean = true) = this.apply {
    forEach {
        it.synced = synced
    }
}

// Checks for valid email address
fun TextView.isValidEmail(): Boolean =
    this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()

// Checks for a valid password
fun TextView.isValidPassword(): Boolean =
    this.isNotEmpty() && this.text.length > 3 && this.isNotNumeric()
