package az.delivery.currencyconverter2.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_currencies")
data class CurrencyDBModel (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "currency_id")
    val id: Int,

    @ColumnInfo(name = "currency_symbol")
    val currency_symbol: String,

    @ColumnInfo(name = "currency_rate")
    val currency_rate: Double,
)