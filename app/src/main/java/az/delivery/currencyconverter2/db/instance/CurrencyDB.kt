package az.delivery.currencyconverter2.db.instance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import az.delivery.currencyconverter2.db.dao.CurrencyDAO
import az.delivery.currencyconverter2.db.model.CurrencyDBModel

@Database(entities = [CurrencyDBModel :: class], version = 1, exportSchema = false)
abstract class CurrencyDB : RoomDatabase() {
    abstract val currencyDAO: CurrencyDAO

    companion object {
        @Volatile
        private var INSTANCE : CurrencyDB? = null
        fun getInstance(context: Context) : CurrencyDB {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CurrencyDB :: class.java,
                        "currency_data_db"
                    )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                }
                return instance
            }
        }
    }
}

