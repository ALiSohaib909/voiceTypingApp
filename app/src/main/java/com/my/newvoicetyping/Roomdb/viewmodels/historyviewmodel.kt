package com.my.newvoicetyping.Roomdb.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.my.newvoicetyping.Roomdb.Database.Historydb
import com.my.newvoicetyping.Roomdb.models.dictionaryhistory
import com.my.newvoicetyping.Roomdb.repositories.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class historyviewmodel(application: Application): AndroidViewModel(application) {

    val allwords : LiveData<List<dictionaryhistory>>
    val repository : repository
    init {

        val dao = Historydb.getDatabase(application).historydao()
        repository = repository(dao)
        allwords = repository.allwords
    }
    fun addwords(dictionaryhistory: dictionaryhistory) = viewModelScope.launch(Dispatchers.IO) {
        repository.inserthistoryitem(dictionaryhistory)
    }

}