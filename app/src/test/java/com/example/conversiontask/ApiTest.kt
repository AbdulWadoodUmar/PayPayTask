package com.example.conversiontask

import com.example.conversiontask.api.service.ApiClient
import com.example.conversiontask.api.service.ApiService
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ApiTest {

    private val params = mutableMapOf<String, Any>().apply {
        put("access_key", "7706d404acea9d26958744bce3788ed6")
        put("format", 1)
    }

    @Test
    fun getCountries() {
        val api = ApiClient.getClient().create(ApiService::class.java)
        val response = api.getCountries(params).execute()

        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun getRates() {
        val api = ApiClient.getClient().create(ApiService::class.java)
        val response = api.getRatesList(params).execute()

        assertThat(response.code()).isEqualTo(200)
    }
}