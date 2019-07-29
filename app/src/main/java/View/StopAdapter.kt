package com.toxa.raspbus.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toxa.raspbus.model.Stop
import android.view.View
import com.toxa.raspbusv2.Activity.activity.Activity.TimeActivity
import kotlinx.android.synthetic.main.stop_item.view.*
import android.view.animation.AnimationUtils
import com.toxa.raspbusv2.Activity.activity.Activity.MainActivity
import com.toxa.raspbusv2.R


@SuppressLint("StaticFieldLeak")
private var context: Context? = null
private var lastPosition = -1

class StopAdapter: RecyclerView.Adapter<StopView>(){
    private val listStop = mutableListOf<Stop>()

    override fun onBindViewHolder(holder: StopView, position: Int) {
    holder.bind(listStop[position])
        holder.itemView.card_view.setOnClickListener{
            context = it.context
            val intent = Intent(context, TimeActivity::class.java)
            intent.putExtra(TimeActivity.pos, holder.adapterPosition)
            intent.putExtra(TimeActivity.pos2, route_Adapter.pos)
            intent.putExtra(TimeActivity.title1, holder.itemView.name_stop.text.toString())
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listStop.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopView {
     val view = LayoutInflater.from(parent.context).inflate(R.layout.stop_item,parent,false)
        return StopView(view)
}

fun set(list: MutableList<Stop>){
    this.listStop.clear()
    this.listStop.addAll(list)
    notifyDataSetChanged()
}



}