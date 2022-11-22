package com.my.newvoicetyping.ui.Speechtotext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.newvoicetyping.R

class countryrvadapter(val clicklistner:countryitemclicklistner):RecyclerView.Adapter<countryrvadapter.countiesrvviewholder>() {

    lateinit var context: Context
    val countrieslist=ArrayList<countrylife>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): countiesrvviewholder {
        context=parent.context
       val view=LayoutInflater.from(parent.context).inflate(R.layout.countriesrvitem,parent,false)

        val countiesrvviewholder=countiesrvviewholder(view)
        view.setOnClickListener {
            clicklistner.oncountryitemclicked(countrieslist[countiesrvviewholder.adapterPosition])
        }
        return countiesrvviewholder
    }

    override fun onBindViewHolder(holder: countiesrvviewholder, position: Int) {
        val currentcountry=countrieslist[position]
        holder.countryname.text=currentcountry.countryname
        Glide.with(context).load(currentcountry.flagicon).into(holder.countryflag)


    }

    override fun getItemCount(): Int {
        return countrieslist.size
    }
    fun getcountryinfo(items:ArrayList<countrylife>)
    {
        countrieslist.clear()
        countrieslist.addAll(items)
        countrieslist.sortBy { it.countryname }
        notifyDataSetChanged()
    }

    class countiesrvviewholder(itemView: View):RecyclerView.ViewHolder(itemView)
    {

        val countryflag:ImageView=itemView.findViewById(R.id.ivcountryflag)
        val countryname:TextView=itemView.findViewById(R.id.tvcountryname)

    }
}