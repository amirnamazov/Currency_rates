package az.delivery.currencyconverter2.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RequestClient {

    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .addInterceptor { chain ->

            val url = chain
                .request()
                .url()
                .newBuilder()
                .addQueryParameter("base", "USD")
                .build()

            val request = chain
                .request()
                .newBuilder()
                .url(url)
                .build()

            chain.proceed(request)
        }
        .build()

    val instance: RequestApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RequestApi::class.java)
    }
}