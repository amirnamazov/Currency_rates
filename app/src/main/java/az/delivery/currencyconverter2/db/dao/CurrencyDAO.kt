package az.delivery.currencyconverter2.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import az.delivery.currencyconverter2.db.model.CurrencyDBModel

@Dao
interface CurrencyDAO {

    @Insert(onConflict = REPLACE)
    fun insertCurrency(currencyDBModel: CurrencyDBModel): Long

    @Update
    fun updateCurrency(currencyDBModel: CurrencyDBModel)

    @Delete
    fun deleteCurrency(currencyDBModel: CurrencyDBModel)

    @Query("DELETE FROM table_currencies")
    fun deleteAll(): Int

    @Query("SELECT * FROM table_currencies")
    fun getAllCurrencies(): List<CurrencyDBModel>
}