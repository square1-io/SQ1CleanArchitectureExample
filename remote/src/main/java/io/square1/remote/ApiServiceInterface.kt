package io.square1.remote

import io.reactivex.Observable
import io.square1.remote.model.Results
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiServiceInterface {

    @GET("?results=25")
    fun getUsers(): Observable<Results>

    companion object {
        fun create(): ApiServiceInterface {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://randomuser.me/api/")
                .build()

            return retrofit.create(ApiServiceInterface::class.java)
        }
    }
}