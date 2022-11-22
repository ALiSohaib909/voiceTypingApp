package com.my.newvoicetyping.Roomdb.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.my.newvoicetyping.Roomdb.daos.historydao
import com.my.newvoicetyping.Roomdb.models.dictionaryhistory


@Database(entities = [dictionaryhistory::class], version = 1, exportSchema = false)
abstract class Historydb:RoomDatabase() {

    abstract fun historydao():historydao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: Historydb? = null

        fun getDatabase(context: Context): Historydb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Historydb::class.java,
                    "history_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}