package com.my.voicetyping.dictionaryviewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.my.newvoicetyping.Roomdb.viewmodels.historyviewmodel



class dictviewmodelfactory(val application: Application):
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when(modelClass){
        historyviewmodel::class.java -> historyviewmodel(application = application)

//        initialassessmentinfoviewmodel::class.java -> initialassessmentinfoviewmodel(application = application, usersrepository = usersrepository)
//        addnewfamilyviewmodel::class.java -> addnewfamilyviewmodel(application = application,usersrepository=usersrepository)
//        communityandfamilyinfoviewmodel::class.java->communityandfamilyinfoviewmodel(application = application,usersrepository=usersrepository)
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T



}