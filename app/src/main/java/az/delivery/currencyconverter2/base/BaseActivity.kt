package az.delivery.currencyconverter2.base

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import az.delivery.currencyconverter2.databinding.ItemProgressBinding
import az.delivery.currencyconverter2.utils.SessionManager
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: AlertDialog

    fun showProgressBar() {
        try {
            val b = ItemProgressBinding.inflate(layoutInflater)
            progressDialog = AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(b.root)
                .create()
            progressDialog.show()

        } catch (e: Exception){
        }
    }

    fun hideProgressBar() {
        try {
            progressDialog.dismiss()
        } catch (e: Exception){}
    }

    fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .show()
    }

    fun isNetworkConnected(): Boolean {
        try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < 23) {
                val ni = cm.activeNetworkInfo
                if (ni != null) {
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                val activeNetwork = cm.activeNetwork
                if (activeNetwork != null) {
                    val nc = cm.getNetworkCapabilities(activeNetwork)
                    return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    )
                }
            }
            return false
        } catch (ex: Exception) {
            return false
        }
    }

    fun getLastRequest(): Long = SessionManager(applicationContext).lastRequest

    fun setLastRequest(lastRequest: Long) {
        SessionManager(applicationContext).lastRequest = lastRequest
    }
}