package com.toxa.RaspBusMVP

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toxa.RaspBusMVP.model.Stop
import com.toxa.RaspBusMVP.View.TimeActivity
import kotlinx.android.synthetic.main.stop_item.view.*


@SuppressLint("StaticFieldLeak")
private var context: Context? = null

class StopAdapter: RecyclerView.Adapter<StopView>(){
    companion object{
        var pos = 0
    }
    private val listStop = mutableListOf<Stop>()

    override fun onBindViewHolder(holder: StopView, position: Int) {
    holder.bind(listStop[position])
        holder.itemView.card_view.setOnClickListener{
            context = it.context
            val intent = Intent(context, TimeActivity::class.java)
            intent.putExtra(TimeActivity.TitleStop, holder.itemView.name_stop.text.toString())
            pos = position
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