package RaspBusMVP

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toxa.raspbus.model.Time
import com.toxa.raspbusv2.R

class TimeAdapter :RecyclerView.Adapter<TimeHolder>(){

    private val listTime = mutableListOf<Time>()

    override fun onBindViewHolder(holder: TimeHolder, position: Int) {
        holder.bind(listTime[position])
    }

    override fun getItemCount(): Int  = listTime.size



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_item,parent,false)
        return TimeHolder(view)
    }

    fun set(list: MutableList<Time>){
        this.listTime.clear()
        this.listTime.addAll(list)
        notifyDataSetChanged()
    }

}