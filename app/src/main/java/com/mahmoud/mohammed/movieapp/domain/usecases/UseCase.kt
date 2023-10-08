package com.mahmoud.mohammed.movieapp.domain.usecases

import com.mahmoud.mohammed.movieapp.domain.common.Transformer
import io.reactivex.Observable

abstract class UseCase<T>(private val transformer: Transformer<T>) {

    abstract fun createObservable(page:Int, data: Map<String, Any>? = null): Observable<T>

    fun observable(page:Int, withData: Map<String, Any>? = null): Observable<T> {
        return createObservable(page, withData).compose(transformer)
    }

}