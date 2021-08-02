package com.example.conversiontask.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.conversiontask.PrefUtil
import com.example.conversiontask.adapters.CurrenciesAdapter
import com.example.conversiontask.api.viewmodel.MainViewModel
import com.example.conversiontask.databinding.ActivityMainBinding
import com.example.conversiontask.models.Currency
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), CurrenciesAdapter.CurrencyListener {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var mBinding: ActivityMainBinding
    private val UPDATE_TIME = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        PrefUtil.initialize(applicationContext)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val adapter = CurrenciesAdapter(this)
        mBinding.recyclerView.adapter = adapter

        viewModel.currenciesList.observe(this) { list ->
            adapter.setList(list)
            hideProgress()
        }
        viewModel.errorMessage.observe(this) { error ->
            showToast(error)
            hideProgress()
        }

        if (isUpdateRequired()) viewModel.getData()
        else adapter.setList(PrefUtil.getCurrencies())
    }


    override fun onItemClick(currency: Currency) {
        startActivity(Intent(this, ConversionActivity::class.java).apply {
            putExtra(ConversionActivity.ITEM_KEY, currency)
        })
    }

    private fun hideProgress() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }

    // check if update time is 30 minutes ago or offline list is empty
    private fun isUpdateRequired(): Boolean {
        val difference = Calendar.getInstance().timeInMillis - PrefUtil.getLastUpdate()
        Log.d(TAG, "D $difference U $UPDATE_TIME")
        return difference >= UPDATE_TIME || PrefUtil.getCurrencies().isEmpty()
    }

}