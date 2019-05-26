@file:JvmName("Constant")

package io.codelabs.zenitech.core

const val NEWSLETTERS_SUBSCRIPTION_TOPIC = "zeniteck-newsletter"
const val THEME_PREFS = "theme_prefs"
const val USER_PREFS = "user_prefs"
const val DATABASE_NAME = "zenitech.db"
const val NOTIFICATION_CHANNEL_NAME = "zenitech-notification-channel"
const val DEFAULT_AVATAR =
    "https://firebasestorage.googleapis.com/v0/b/house-teaching.appspot.com/o/default-avatar.png?alt=media&token=b1ab3898-f830-42b6-aa08-18c70627bb87"

// VIEWMODEL
const val USER_VM = "user_view_model"
const val PRODUCT_VM = "product_view_model"

// DATABASE
const val CUSTOMER = "customers"
const val PRODUCTS = "products"
const val CART = "$CUSTOMER/%s/cart"
const val WISHLIST = "$CUSTOMER/%s/wishlist"
const val PRODUCTS_ITEM = "products/%s"