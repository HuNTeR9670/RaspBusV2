package com.toxa.raspbus.View

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.toxa.raspbus.model.Time
import kotlinx.android.synthetic.main.stop_item.view.*
import kotlinx.android.synthetic.main.time_item.view.*

class TimeHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

       fun bind(time: Time) {
        itemView.curr_time.text = time.time
    }
}