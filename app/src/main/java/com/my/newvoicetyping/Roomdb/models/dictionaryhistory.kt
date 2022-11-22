package com.my.newvoicetyping.Roomdb.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history_table")
data class dictionaryhistory(val word:String,val definition:String){
    @PrimaryKey(autoGenerate = true) var id = 0
}
