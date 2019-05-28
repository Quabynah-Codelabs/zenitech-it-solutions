package io.codelabs.zenitech.core.dbutil

data class LoginRequest(val email: String, val password: String)

data class OAuthRequest(val email: String?, val accountId: String, var username: String?, var avatar: String?)

data class CustomerRequest(val key: String)

data class CartRequest(val user: String?, val product: String?)