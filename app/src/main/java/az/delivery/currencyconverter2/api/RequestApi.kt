package az.delivery.currencyconverter2.api

import az.delivery.currencyconverter2.api.model.CurrencyRateModel
import retrofit2.Call
import retrofit2.http.*

interface RequestApi {

    @GET("latest")
    fun getLatestCurrencyRates(): Call<CurrencyRateModel>

    @GET("timeseries")
    fun getCurrencyRatesBetweenTimeRange(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("symbols") symbol: String
    ): Call<CurrencyRateModel>
}