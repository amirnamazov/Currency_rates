package az.delivery.currencyconverter2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import az.delivery.currencyconverter2.databinding.ItemCurrencyBinding

import az.delivery.currencyconverter2.activity.MainActivity
import az.delivery.currencyconverter2.db.model.CurrencyDBModel

class CurrencyAdapter(private val a: MainActivity, private val list: List<CurrencyDBModel>)
    : RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>() {

    private lateinit var b: ItemCurrencyBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        b = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyHolder(b.root)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = position

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            b.txtCurrencyName.text = list[position].currency_symbol
            b.txtCurrencyRate.text = list[position].currency_rate.toString()

            b.root.setOnClickListener {
                a.onItemClick(position)
            }
        }
    }

    interface ItemClick {
        fun onItemClick(position: Int)
    }
}