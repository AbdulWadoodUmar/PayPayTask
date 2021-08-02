package com.example.conversiontask.api.repository

import com.example.conversiontask.api.service.ApiService
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Response

class MainRepository constructor(private val apiService: ApiService) {

    private val params = mutableMapOf<String, Any>().apply {
        put("access_key", "f39ef100dabff429b90958519927d2ee")
        put("format", 1)
    }

    fun getList(): Single<Response<ResponseBody>> {
        return apiService.getList(params)
    }

    fun getRates(): Single<Response<ResponseBody>> {
        return apiService.getRates(params)
    }
}