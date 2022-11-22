package com.my.newvoicetyping.Roomdb.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.my.newvoicetyping.Roomdb.daos.historydao
import com.my.newvoicetyping.Roomdb.models.dictionaryhistory

class repository(val historydao: historydao) {



    val allwords: LiveData<List<dictionaryhistory>> = historydao.getallworddefinitions()
    suspend fun inserthistoryitem(dictionaryhistory: dictionaryhistory)
    {
        historydao.addworddefinition(dictionaryhistory)
    }






}