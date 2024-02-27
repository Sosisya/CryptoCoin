package com.example.cryptocoin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptocoin.Api.ApiFactory
import com.example.cryptocoin.dataBase.AppDatabase
import com.example.cryptocoin.pojo.CoinPriceInfo
import com.example.cryptocoin.pojo.CoinPriceInfoRowData
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application): AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisplosable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    init {
        loadData()
    }

    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

   private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo()
            .map { it.data?.map { it.coinInfo?.name }?.joinToString(",").toString() }
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(10, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                Log.d("Success", it.toString())
            }, {
                Log.d("ERROR", it.message.toString())
            })
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRowData:
        // Приходит объект Raw JSON
        CoinPriceInfoRowData
    ): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRowData.coinPriceInfoJsonObject?: return result
        //Берем у JSON объекта все ключи.
        val coinKeySet = jsonObject.keySet()
        //Проходим по всем ключам
        for (coinKey in coinKeySet) {
            //каждый полученный ключ содержит еще 1 ключ
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            //проходимся по следующему ключу
            for (currencyKey in currencyKeySet) {
                //Тут уже получаем JSON объект
                val priceInfo = Gson().fromJson(
                    //конвертируем его в CoinPriceInfo
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                //добавляем его в нашу коллекцию
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisplosable.dispose()
    }
}