package com.example.cryptocoin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocoin.R
import com.example.cryptocoin.databinding.ItemCoinInfoBinding
import com.example.cryptocoin.pojo.CoinInfo
import com.example.cryptocoin.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso

class CoinInfoAdapter(private val context: Context): RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onCoinClickListener: OnCoinClickListener? = null

    inner class CoinInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding: ItemCoinInfoBinding = ItemCoinInfoBinding.bind(itemView)

        fun bind(coin: CoinInfo) {
            val ivLogoCoin = binding.ivLogoCoin
            val tvSymbols = binding.tvSymbols
            val priceSymbols = binding.priceSymbols
            val updateSymbols = binding.updateSymbols
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info, parent, false)
        return CoinInfoViewHolder(view)
    }

    override fun getItemCount() = coinInfoList.size

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder.binding) {
            with(coin) {
                val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
                tvSymbols.text = String.format(symbolsTemplate, fromsymbol,tosymbol)
                priceSymbols.text = price.toString()
                updateSymbols.text = String.format(lastUpdateTemplate, getFormattedTime())
                Picasso.get().load(coin.getFullImageURL()).into(ivLogoCoin)
            }
        }
        holder.itemView.setOnClickListener() {
            onCoinClickListener?.onCoinClick(coin)
        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)

    }
}