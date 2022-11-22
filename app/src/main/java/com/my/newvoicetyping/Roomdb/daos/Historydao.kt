package com.my.newvoicetyping.Roomdb.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.my.newvoicetyping.Roomdb.models.dictionaryhistory


@Dao
interface historydao {

    @Insert
    fun addworddefinition(dictionaryhistory: dictionaryhistory)


    @Query("SELECT * FROM history_table")
    fun getallworddefinitions():LiveData<List<dictionaryhistory>>

}