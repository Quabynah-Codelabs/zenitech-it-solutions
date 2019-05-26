package io.codelabs.zenitech.core.messaging

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.codelabs.sdk.util.createNotificationChannel
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.pushNotification
import io.codelabs.zenitech.core.NOTIFICATION_CHANNEL_NAME
import io.codelabs.zenitech.core.datasource.repository.Preferences
import io.codelabs.zenitech.ui.HomeActivity

/**
 * Notification service for Application
 */
class MessagingService : FirebaseMessagingService() {

    private val prefs: Preferences by lazy { Preferences.getInstance(applicationContext) }

    override fun onMessageReceived(rm: RemoteMessage?) {
        if (rm == null) {
            debugLog("Remote message not found for this project")
            return
        }

        // Extract notification data
        val data = rm.data

        // Send notification to only logged in users
        if (prefs.isLoggedIn) {
            createNotificationChannel(NOTIFICATION_CHANNEL_NAME)
            pushNotification(
                data["title"],
                data["message"],
                Intent(applicationContext, HomeActivity::class.java)/*R.drawable.sample_image*/
            )
        }
    }
}