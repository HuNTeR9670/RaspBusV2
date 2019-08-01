package com.toxa.RaspBusMVP

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toxa.RaspBusMVP.model.Time

class TimeAdapter :RecyclerView.Adapter<TimeHolder>(){

    private val listTime = mutableListOf<Time>() // объявление списка времени прибытия

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeHolder { // метод создания ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_item,parent,false) // указываем макет для каждого элемента RecyclerView
        return TimeHolder(view) // возращаем макет ViewHolder
    }

    override fun onBindViewHolder(holder: TimeHolder, position: Int) {
        holder.bind(listTime[position]) // привязка информации к объекту
    }

    override fun getItemCount(): Int  = listTime.size // метод возвращает количество элементов в списке


    fun set(list: MutableList<Time>){ // метод добавления элементов в список
        this.listTime.clear() // очистка списка
        this.listTime.addAll(list) // добавление элементов
        notifyDataSetChanged() // обновление изменений
    }

}