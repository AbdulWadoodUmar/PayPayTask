package com.example.conversiontask.api.service

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

@JvmSuppressWildcards
interface ApiService {

    @GET("list")
    fun getList(@QueryMap(encoded = true) query: Map<String, Any>): Single<Response<ResponseBody>>

    @GET("live")
    fun getRates(@QueryMap(encoded = true) query: Map<String, Any>): Single<Response<ResponseBody>>


    // testing methods
    @GET("list")
    fun getCountries(@QueryMap(encoded = true) query: Map<String, Any>): Call<ResponseBody>

    @GET("live")
    fun getRatesList(@QueryMap(encoded = true) query: Map<String, Any>):Call<ResponseBody>
}