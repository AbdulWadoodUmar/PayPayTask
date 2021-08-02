package com.example.conversiontask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.conversiontask.databinding.CurrenciesListViewBinding
import com.example.conversiontask.models.Currency

class CurrenciesAdapter(val listener: CurrencyListener) :
    RecyclerView.Adapter<CurrenciesAdapter.CurrenciesHolder>() {

    private var list = mutableListOf<Currency>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CurrenciesListViewBinding.inflate(inflater, parent, false)
        return CurrenciesHolder(view)
    }

    override fun onBindViewHolder(holder: CurrenciesHolder, position: Int) {
        val item = list[position]
        holder.setData(item)
        holder.itemView.setOnClickListener { listener.onItemClick(item) }
    }

    fun setList(list: MutableList<Currency>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    interface CurrencyListener {
        fun onItemClick(currency: Currency)
    }

    inner class CurrenciesHolder(private val view: CurrenciesListViewBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun setData(currency: Currency) {
            view.titleView.text = currency.name
            view.rateView.text = String.format("$%.3f", currency.rate)
        }
    }
}