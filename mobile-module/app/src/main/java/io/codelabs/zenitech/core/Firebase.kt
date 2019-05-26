package io.codelabs.zenitech.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

/**
 * [Firebase] APIs
 */
object Firebase {
    val storage = FirebaseStorage.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val messaging = FirebaseMessaging.getInstance()
}