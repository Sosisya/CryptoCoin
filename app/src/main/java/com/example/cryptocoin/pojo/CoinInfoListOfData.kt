package com.example.cryptocoin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CoinInfoListOfData {
    @SerializedName("Data")
    @Expose
    val data: List<Datum>? = null
}