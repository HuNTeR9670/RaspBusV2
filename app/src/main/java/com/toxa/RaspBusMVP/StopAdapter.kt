package com.toxa.RaspBusMVP

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toxa.RaspBusMVP.model.Stop
import com.toxa.RaspBusMVP.View.TimeActivity
import kotlinx.android.synthetic.main.stop_item.view.*


class StopAdapter: RecyclerView.Adapter<StopHolder>(){ // адаптер списка остановок

    companion object{
        var pos = 0 // объявление вспомогательного объекта
    }
    private val listStop = mutableListOf<Stop>() // объявление списка остановок

    private var context: Context? = null //объявление контекста

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopHolder { // метод создания ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stop_item,parent,false) // указываем макет для каждого элемента RecyclerView
        return StopHolder(view) // передача макета ViewHolder
    }

    override fun onBindViewHolder(holder: StopHolder, position: Int) {
    holder.bind(listStop[position]) // привязка данных к объекту
        holder.itemView.card_view.setOnClickListener{// обработка нажатия на элемент
            context = it.context
            val intent = Intent(context, TimeActivity::class.java)
            intent.putExtra(TimeActivity.TitleStop, holder.itemView.name_stop.text.toString()) // передача наимменования остановки
            pos = position //передача позиции элемента
            it.context.startActivity(intent) // старт Activity
        }
    }

    override fun getItemCount(): Int = listStop.size // метод возвращает количество элементов в списке


fun set(list: MutableList<Stop>){ // метод добавления элементов в список
    this.listStop.clear() // очистка списка
    this.listStop.addAll(list) // добавление элементовв список
    notifyDataSetChanged() // обновление изменений
}



}