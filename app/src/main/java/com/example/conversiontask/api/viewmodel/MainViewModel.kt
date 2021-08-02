package com.example.conversiontask.api.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conversiontask.PrefUtil
import com.example.conversiontask.api.repository.MainRepository
import com.example.conversiontask.api.service.ApiClient
import com.example.conversiontask.api.service.ApiService
import com.example.conversiontask.models.Currency
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject
import java.util.*

class MainViewModel : ViewModel() {

    private val apiService = ApiClient.getClient().create(ApiService::class.java)
    private val repository = MainRepository(apiService)
    private val disposable = CompositeDisposable()
    var currenciesList = MutableLiveData<MutableList<Currency>>()
    val errorMessage = MutableLiveData<String>()


    fun getData() {
        disposable.add(getList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { currencies -> mapRates(currencies) }
            .subscribeBy(
                onError = { errorMessage.postValue(it.message ?: "Failed to load data") },
                onNext = { currencies ->
                    if (currencies.any { it.rate != 0.0 }) { // check if rates are not 0
                        currenciesList.postValue(currencies)
                        PrefUtil.setCurrencies(currencies)
                        PrefUtil.setLastUpdate(Calendar.getInstance().timeInMillis)
                    } else { // update offline data
                        currenciesList.postValue(PrefUtil.getCurrencies())
                    }
                }
            )
        )
    }

    private fun getList(): Observable<MutableList<Currency>> {
        return repository.getList()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                val list = mutableListOf<Currency>()
                if (response.isSuccessful) {
                    response.body()?.string()?.let { string ->
                        val result = JSONObject(string)
                        if (result.isSuccess() && result.isValid("currencies")) {
                            val currencies = result.getJSONObject("currencies")
                            currencies.keys().forEach { key ->
                                list.add(Currency(currency = key, name = currencies.getString(key)))
                            }
                        } else {
                            if (result.isValid("error")) {
                                val error = result.getJSONObject("error")
                                if (error.isValid("info")) {
                                    errorMessage.postValue(error.getString("info"))
                                }
                            }
                        }
                    }
                }
                list
            }
    }

    // fetch all the rates and map them to list
    private fun mapRates(currency: MutableList<Currency>): Observable<MutableList<Currency>> {
        return repository.getRates()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                if (response.isSuccessful) {
                    response.body()?.string()?.let { string ->
                        val result = JSONObject(string)
                        if (result.isSuccess() && result.isValid("quotes")) {
                            val source = result.getString("source")
                            val quotes = result.getJSONObject("quotes")

                            currency.forEach { currency ->
                                val name = "$source${currency.currency}"
                                if (quotes.has(name)) {
                                    currency.rate = quotes.getDouble(name)
                                }
                            }
                        } else {
                            if (result.isValid("error")) {
                                val error = result.getJSONObject("error")
                                if (error.has("info")) {
                                    errorMessage.postValue(error.getString("info"))
                                }
                            }
                        }
                    }
                }
                currency
            }
    }

    private fun JSONObject.isSuccess(): Boolean {
        return has("success") && getBoolean("success")
    }

    private fun JSONObject.isValid(input: String): Boolean {
        return has(input) && !isNull(input) && getJSONObject(input).length() > 0
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}