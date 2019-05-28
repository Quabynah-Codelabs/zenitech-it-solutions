package io.codelabs.zenitech.core.dbutil

interface Callback<Type> {

    fun onInit() {}

    fun onComplete() {}

    fun onError(error: String?)

    fun onSuccess(result: Type)
}