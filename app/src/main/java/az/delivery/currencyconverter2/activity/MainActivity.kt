package az.delivery.currencyconverter2.activity

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import az.delivery.currencyconverter2.adapter.CurrencyAdapter
import az.delivery.currencyconverter2.api.model.CurrencyRateModel
import az.delivery.currencyconverter2.base.BaseActivity
import az.delivery.currencyconverter2.databinding.ActivityMainBinding
import az.delivery.currencyconverter2.api.RequestClient
import az.delivery.currencyconverter2.db.instance.CurrencyDB
import az.delivery.currencyconverter2.db.model.CurrencyDBModel
import az.delivery.currencyconverter2.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : BaseActivity(), CurrencyAdapter.ItemClick {

    private lateinit var b: ActivityMainBinding
    private val currencyDao by lazy { CurrencyDB.getInstance(this).currencyDAO }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (isNetworkConnected()) {
            if (Date().time - getLastRequest() >= 1000*60*10) {
                setLastRequest(Date().time)
                getCurrencyRate()
            } else {
                setAdapter()
            }
        } else {
            setAdapter()
            showErrorDialog("No internet connection")
        }
    }

    private fun getCurrencyRate() {

        showProgressBar()

        RequestClient.instance.getLatestCurrencyRates().enqueue(object : Callback<CurrencyRateModel>{
            override fun onResponse(call: Call<CurrencyRateModel>, response: Response<CurrencyRateModel>) {
                if (response.isSuccessful) {
                    val map = response.body()!!.rates as Map<String, Double>
                    currencyDao.deleteAll()
                    map.entries.forEach {
                        currencyDao.insertCurrency(CurrencyDBModel(0, it.key, it.value))
                    }

                    setAdapter()

                } else {
                    showErrorDialog("${response.code()} ${response.message()}")
                }
                hideProgressBar()
            }

            override fun onFailure(call: Call<CurrencyRateModel>, t: Throwable) {
                showErrorDialog(t.message!!)
                hideProgressBar()
            }
        })
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, CurrencyHistoryActivity :: class.java).apply {
            putExtra("SYMBOL", currencyDao.getAllCurrencies()[position].currency_symbol)
        })
    }

    private fun setAdapter() {
        b.rvCurrency.layoutManager = LinearLayoutManager(this)
        b.rvCurrency.adapter = CurrencyAdapter(this, currencyDao.getAllCurrencies())
    }
}