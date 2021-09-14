package az.delivery.currencyconverter2.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import az.delivery.currencyconverter2.api.model.CurrencyRateModel
import az.delivery.currencyconverter2.base.BaseActivity
import az.delivery.currencyconverter2.databinding.ActivityCurrencyHistoryBinding
import kotlin.collections.ArrayList
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import az.delivery.currencyconverter2.api.RequestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.jjoe64.graphview.DefaultLabelFormatter

class CurrencyHistoryActivity : BaseActivity() {

    private lateinit var b: ActivityCurrencyHistoryBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCurrencyHistoryBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (isNetworkConnected()) {
            getCurrencyRatesBetweenTimeRange()
        } else {
            showErrorDialog("No internet connection")
        }
    }

    private fun getCurrencyRatesBetweenTimeRange() {

        showProgressBar()

        val symbol = intent.getStringExtra("SYMBOL")
        val today = DateFormat.format("yyyy-MM-dd", Date())
        val sevenDaysAgo = DateFormat.format("yyyy-MM-dd", Date(Date().time - 1000*60*60*24*7))

        RequestClient.instance.getCurrencyRatesBetweenTimeRange(sevenDaysAgo.toString(), today.toString(), symbol!!).enqueue(object : Callback<CurrencyRateModel> {
            override fun onResponse(call: Call<CurrencyRateModel>, response: Response<CurrencyRateModel>) {
                if (response.isSuccessful) {
                    if (response.body()!!.rates != null) {
                        val map = response.body()!!.rates as Map<String, Any>
                        setupGraph(map, symbol)
                    } else {
                        showErrorDialog("No exchange rate data is available for the selected currency")
                    }
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

    private fun setupGraph(map: Map<String, Any>, symbol: String) {

        val days = ArrayList<Date>()
        val rates = ArrayList<Double>()

        map.entries.forEach {
            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.key)
            days.add(date)

            try {
                val rateMap = it.value as Map<String, Double>
                rates.add(rateMap[symbol]!!)
            } catch (e: Exception){ }
        }

        val series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()
        for (i in days.indices) {
            try {
                series.appendData(DataPoint(days[i], rates[i]), true, 20)
            } catch (e: Exception){ }
        }

        series.color = Color.GREEN
        series.title = ""
        series.isDrawDataPoints = true
        series.dataPointsRadius = 5f
        series.thickness = 2

        b.graph.addSeries(series)
        b.graph.title = "Currency in last 7 days"
        b.graph.titleTextSize = 50f
        b.graph.titleColor = Color.RED
        b.graph.legendRenderer.isVisible = false
        b.graph.viewport.isScalable = true
        b.graph.viewport.isScrollable = true

        val gridLabel: GridLabelRenderer = b.graph.gridLabelRenderer
        gridLabel.horizontalAxisTitle = "Date"
        gridLabel.verticalAxisTitle = "Currency rate"
        gridLabel.setHorizontalLabelsAngle(90)
        gridLabel.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) DateFormat.format("yyyy-MM-dd", value.toLong()).toString()
                else super.formatLabel(value, isValueX)
            }
        }
    }
}