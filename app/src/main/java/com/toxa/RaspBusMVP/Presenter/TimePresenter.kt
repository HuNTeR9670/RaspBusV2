package com.toxa.RaspBusMVP.Presenter

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.View
import com.toxa.RaspBusMVP.StopAdapter
import com.toxa.RaspBusMVP.TimeAdapter
import com.toxa.RaspBusMVP.View.TimeActivity
import com.toxa.RaspBusMVP.model.Time
import com.toxa.RaspBusMVP.route_Adapter
import kotlinx.android.synthetic.main.activity_time.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

private val listTime = mutableListOf<Time>() // список времени
lateinit var adapterTime: TimeAdapter // адаптер списка времен

private var Url = arrayOf("http://ap2polotsk.of.by/ap2/rasp/gorod/m-1/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-2/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-2a/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-3/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-4/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-4a/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-6/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-7/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-8/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-9/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-11/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-13/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-23/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-24/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-26/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-27/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-28-00/",
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-28/") //массив ссылок

class TimePresenter : AsyncTask<Void, Void, MutableList<Time>>() {// поток парсинга страницы и получения данных в адаптер

    override fun doInBackground(vararg params: Void): MutableList<Time> {
        val doc: Document
        var flag = 0
        val yourDate = Calendar.getInstance().time // получение текущего времени
        val c = Calendar.getInstance()
        c.time = yourDate

        val dayOfWeek = c[Calendar.DAY_OF_WEEK]
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) { // если выхлдной то присваиваем флагу 1
            flag = 1
        }
        val pos = StopAdapter.pos //получаем позици по остановке
        val pos_URL = route_Adapter.pos // получаем позицию по маршруту
        try {
            listTime.clear() // очищаем список
            doc = Jsoup.connect(Url[pos_URL]).get() // по полученой позиции получаем URL парсим страницу
            val table = doc.select("table")[flag] // получаем таблицу в случае если выходной то таблица №2
            val rows = table.select("tr") // получаем столбцы таблицы
            val row = rows[pos+1] //по номеру индекса получает строку
            val cols = row.select("td")// разбиваем полученную строку по тегу  на столбы
            val str1 = cols[1].text() //получаем строку значений времни из таблицы
            val Time_List = str1.split("|") // разделяем строку
            for (i in 0 until Time_List.size){ // цикл пока не будет достигнут конец спика значений времени
                val str2 = Time_List[i]
                listTime.add(Time(str2)) // заполняем список значениями
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listTime
    }


    override fun onPostExecute(result: MutableList<Time>) {
        //записываем данные в адаптер
        adapterTime.set(result)
    }
}