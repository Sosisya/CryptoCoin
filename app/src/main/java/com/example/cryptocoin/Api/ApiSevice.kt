package com.example.cryptocoin.Api

import com.example.cryptocoin.pojo.CoinInfoListOfData
import com.example.cryptocoin.pojo.CoinPriceInfoRowData
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Currency

interface ApiSevice {
    @GET("top/totalvolfull")
    fun getTopCoinsInfo(
        @Query(QUERY_PARAM_APIKEY) apiKey: String = "9aa2bad7faf85650113219744ceb3bf1710bcdfcf1a4e997baf67e2ab4ad1db5",
        @Query(QUERY_PARAM_LIMIT) limit: Int = 50,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CURRENCY
    ) : Single<CoinInfoListOfData>

    @GET("pricemultifull")
    fun getFullPriceList(
        @Query(QUERY_PARAM_APIKEY) apiKey: String = "9aa2bad7faf85650113219744ceb3bf1710bcdfcf1a4e997baf67e2ab4ad1db5",
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CURRENCY,
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String
    ) : Single<CoinPriceInfoRowData>

    companion object {
        private const val QUERY_PARAM_APIKEY = "api_key"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"

        private const val CURRENCY = "USD"
    }
}