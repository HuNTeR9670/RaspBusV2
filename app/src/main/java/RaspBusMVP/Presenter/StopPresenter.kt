package RaspBusMVP.Presenter

import RaspBusMVP.StopAdapter
import RaspBusMVP.View.StopActivity
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.View
import com.toxa.raspbus.model.Stop
import kotlinx.android.synthetic.main.activity_stop.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*


private val listRoute = mutableListOf<Stop>()
lateinit var adapterStop: StopAdapter

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
    "http://ap2polotsk.of.by/ap2/rasp/gorod/m-28/")

class StopPresenter : AsyncTask<Void, Void, MutableList<Stop>>() { // поток парсинга страницы и получения данных в адаптер

    override fun doInBackground(vararg params: Void): MutableList<Stop> {
        val doc: Document
        var tb = 0
        val yourDate = Calendar.getInstance().time
        val c = Calendar.getInstance() // получение текущего времени
        c.time = yourDate
        val dayOfWeek = c[Calendar.DAY_OF_WEEK]
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) { // если выходной то
            tb = 1 // присваиваем флагу 1
        }
        val pos = StopAdapter.pos
        try {
            listRoute.clear()
            doc = Jsoup.connect(Url[pos]).get() // по полученой позиции получаем URL парсим страницу
            val table = doc.select("table")[tb] // получаем таблицу в случае если выходной то таблица №2
            val rows = table.select("tr") // получаем строки таблицы
            val j: Int
            j = if (pos==3){
                3
            } else {
                rows.size
            }
            for (i in 1 until j) {
                val row = rows[i] //по номеру индекса получает строку
                val cols = row.select("td")// разбиваем полученную строку по тегу  на столбы
                val str1 = cols[0].text()  // получаем названия остановок
                if (str1=="—"){}else // если таблица не имеет ничего
                    listRoute.add(Stop(str1))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listRoute // передача списка остановок
    }


    override fun onPostExecute(result: MutableList<Stop>) {
        //запиываем данные в адаптер
        adapterStop.set(result)

    }
}