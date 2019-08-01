package com.toxa.RaspBusMVP

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toxa.RaspBusMVP.View.StopActivity
import kotlinx.android.synthetic.main.route_item.view.*
import com.toxa.RaspBusMVP.model.Route

class route_Adapter: RecyclerView.Adapter<routeHolder>() { // адаптер списка маршрутов
    private val listRoute = mutableListOf<Route>() // объявление списка маршрутов

    companion object {
        var pos = 0   //объявление вспомогательного объекта
    }

    private var context: Context? = null //объявление контекста

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): routeHolder { // метод создания ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.route_item,parent,false) // указываем макет для каждого элемента RecyclerView
        return routeHolder(view) // передача макета ViewHolder
    }

    override fun onBindViewHolder(holder: routeHolder, position: Int) {
        holder.bind(listRoute[position]) //привязка данных к объекту
        holder.itemView.card_view.setOnClickListener{ // обработка нажатия на элемент
            context = it.context
            val intent = Intent(context, StopActivity::class.java)
            intent.putExtra(StopActivity.PrevTitle, holder.itemView.Route_Name.text) // передача наименования маршрута
            pos = position // передача текущей позиции
            it.context.startActivity(intent) // старт нового Activity
        }
    }

    override fun getItemCount() = listRoute.size //метод возвращает количество элементов в списке

    fun set(list: MutableList<Route>){ // метод добавление элементов в список
        this.listRoute.clear() // очистка списка
        this.listRoute.addAll(list) // добавление элементов в список
        notifyDataSetChanged() //обновление изменений
    }
}