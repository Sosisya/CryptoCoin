package com.example.cryptocoin.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptocoin.pojo.CoinPriceInfo

@Dao
interface CoinPriceInfoDao {


    // МЕТОД ПОЙДЕТ В РЕСАЙКЛЕР
    @Query("SELECT * FROM full_price_list ORDER BY lastupdate DESC")
    fun getPriceList(): LiveData<List<CoinPriceInfo>>

    //ЭТОТ МЕТОД О ИНФОРМАЦИИ ПО 1 ВАЛЮТЕ
    @Query("SELECT * FROM full_price_list WHERE fromsymbol == :fSym LIMIT 1")
    fun getPriceInfoAboutCoin(fSym: String): LiveData<CoinPriceInfo>

    // МЕТОД БУДЕТ СОХРАНЯТЬ ПОЛУЧЕННЫЕ ИЗ ИНТЕРНЕТА ДАННЫЕ В БАЗУ
    @Insert(onConflict = OnConflictStrategy.REPLACE)  //это строчка говорит нам о том что данные которые приходят из интернета постоянно будут заменяться на последние
    fun insertPriceList(priceList: List<CoinPriceInfo>)
}