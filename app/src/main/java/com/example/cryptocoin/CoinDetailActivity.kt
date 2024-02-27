package com.example.cryptocoin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocoin.databinding.ActivityCoinDetailBinding
import com.squareup.picasso.Picasso

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinDetailBinding
    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }
        var fromSymbol: String? = intent.getStringExtra(EXTRA_FROM_SYMBOL)
        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        viewModel.getDetailInfo(fromSymbol!!).observe(this, Observer {
            binding.tvPrice.text = it.price.toString()
            binding.tvFromSymbol.text = it.fromsymbol
            binding.tvToSymbol.text = it.tosymbol.toString()
            binding.tvMinPrice.text = it.lowday.toString()
            binding.tvMaxPrice.text = it.lastmarket.toString()
            binding.tvUpdated.text = it.getFormattedTime().toString()
            Picasso.get().load(it.getFullImageURL()).into(binding.ivLogoCoin)
        })
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}