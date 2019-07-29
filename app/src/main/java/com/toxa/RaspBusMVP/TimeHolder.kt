package com.toxa.RaspBusMVP

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.toxa.RaspBusMVP.model.Time
import kotlinx.android.synthetic.main.time_item.view.*

class TimeHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

       fun bind(time: Time) {
        itemView.curr_time.text = time.time
    }
}