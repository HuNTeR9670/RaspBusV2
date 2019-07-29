package com.toxa.RaspBusMVP.Presenter

import com.toxa.RaspBusMVP.model.Route
import com.toxa.RaspBusMVP.route_Adapter
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


private val listImage = mutableListOf<Route>()
@SuppressLint("StaticFieldLeak")
lateinit var adapterRoute: route_Adapter
val array = arrayOf("АВТОВОКЗАЛ-СЕЛЬХОЗТЕХНИКА","БОЛЬНИЦА (Ксты)-Ж/Д БОЛЬНИЦА (Новка)",
    "БОЛЬНИЦА (Ксты)-Ж/Д БОЛЬНИЦА (Новка)","АВТОВОКЗАЛ-ЭЛЕКТРОСЕТИ",
    "МАРИНЕНКО-БОРОВУХА-2","МАРИНЕНКО-БОРОВУХА-2","АВТОВОКЗАЛ-КАЛИНИНА","АВТОВОКЗАЛ-ГВАРДЕЕЦ",
    "АВТОВОКЗАЛ-БАБУШКИНА","АВТОВОКЗАЛ-КСТЫ","АВТОВОКЗАЛ-ТРОСНИЦКАЯ","АВТОВОКЗАЛ-ГОРТОП",
    "Мариненко-Ксты","АВТОВОКЗАЛ-КСТЫ ч/з Аэродром","МАРИНЕНКО-АЭРОДРОМ-АВТОВОКЗАЛ",
    "АВТОВОКЗАЛ-АЭРОДРОМ","МАРИНЕНКО-АВТОВОКЗАЛ-АЭРОДРОМ","МАРИНЕНКО-АВТОВОКЗАЛ-АЭРОДРОМ")

class MainPresenter : AsyncTask<Void, Void, MutableList<Route>>() { // поток парсинга страницы и получения данных в адаптер


    override fun doInBackground(vararg params: Void): MutableList<Route> {
        val doc: Document
        try {
            doc = Jsoup.connect("http://ap2polotsk.of.by/ap2/rasp/gorod/").get() // получение страницы
            val png = doc.select("img[src$=.png]") // выделение из страницы картинок
            for (i in 0 until png.size ){ // пока не будет достингнут конец списка картинок
                listImage.add(  // добавлене ссылок на картинки
                    Route(
                        array[i],
                        png.eq(i).attr("src")
                    )
                )
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listImage // передача списка кариток
    }


    override fun onPostExecute(result: MutableList<Route>) {
        //установка адаптера списка
        adapterRoute.set(result)
    }
}