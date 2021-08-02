package com.example.conversiontask.views

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.conversiontask.databinding.ActivityConversionBinding
import com.example.conversiontask.models.Currency

class ConversionActivity : AppCompatActivity() {

    companion object {
        const val ITEM_KEY = "ConversionActivity.ITEM_KEY"
    }

    private lateinit var mBinding: ActivityConversionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityConversionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val currency = intent?.getSerializableExtra(ITEM_KEY) as Currency?
        currency?.let {
            mBinding.fromKey.text = currency.currency
            mBinding.inputFrom.setText("1")
            mBinding.inputFrom.setSelection(1)

            mBinding.inputTo.setText(String.format("%.3f", currency.rate))

            if (currency.rate > 0.0) {
                mBinding.inputFrom.doAfterTextChanged { text ->
                    text?.let {
                        if (it.isNotEmpty()) {
                            var input = it.toString().toInt()
                            input = if (input > 0) input else 1
                            val result = input * currency.rate
                            mBinding.inputTo.setText(String.format("%.3f", result))
                        }
                    }
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}