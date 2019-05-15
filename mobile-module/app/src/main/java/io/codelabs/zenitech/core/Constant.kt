@file:JvmName("Constant")

package io.codelabs.zenitech.core

const val THEME_PREFS = "theme_prefs"
const val USER_PREFS = "user_prefs"
const val DATABASE_NAME = "zenitech.db"
const val DEFAULT_AVATAR = ""

// VIEWMODEL
const val USER_VM = "user_view_model"

// DATABASE
const val CUSTOMER = "customers"
const val PRODUCTS = "products"
const val CART = "$CUSTOMER/%s/cart"
const val WISHLIST = "$CUSTOMER/%s/wishlist"
const val PRODUCTS_ITEM = "products/%s"