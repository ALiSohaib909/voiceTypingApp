package com.my.newvoicetyping.ui.Themes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.newvoicetyping.R

class ThemesAdapter(val onThemeClick: themeCheckListener) :
    RecyclerView.Adapter<ThemesAdapter.ViewHolder>() {

    val list = ArrayList<Int>()
    lateinit var context: Context
    lateinit var checkClick: themeCheckListener

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layoutkeyboardthemes, parent, false)
        val pos = ViewHolder(view)
        view.setOnClickListener {
            onThemeClick.onThemeChecked(list[pos.adapterPosition])
        }

        return pos
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        Glide.with(context).load(currentItem).into(holder.theme)
    }

    fun getthemesimages(mylist: ArrayList<Int>) {
        list.clear()
        list.addAll(mylist)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val theme = itemView.findViewById<ImageView>(R.id.ivkeyboardtheme)
    }
}