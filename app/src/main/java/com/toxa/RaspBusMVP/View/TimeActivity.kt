package com.toxa.RaspBusMVP.View

import com.toxa.RaspBusMVP.TimeAdapter
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.toxa.RaspBusMVP.R
import com.toxa.RaspBusMVP.model.Time
import kotlinx.android.synthetic.main.activity_time.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*


private val listTime = mutableListOf<Time>()
private lateinit var adapter: TimeAdapter

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

private var TitleBlet = ""


class TimeActivity : AppCompatActivity() {

    companion object {

        const val pos = "posl"
        const val pos2 = "pos2"
        const val title1 = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter)
        val actionBar = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(title1)
        TitleBlet = intent.getStringExtra(title1)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return true
    }

    @SuppressLint("StaticFieldLeak")
    inner class MyTask : AsyncTask<Void, Void, MutableList<Time>>() {

        override fun doInBackground(vararg params: Void): MutableList<Time> {
            val doc: Document
            var tb = 0
            val yourDate = Calendar.getInstance().time
            val c = Calendar.getInstance()
            c.time = yourDate

            val dayOfWeek = c[Calendar.DAY_OF_WEEK]
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                tb = 1
            }
            val count = intent.getIntExtra(pos,0)
            val count1 = intent.getIntExtra(pos2,0)
            try {
                doc = Jsoup.connect(Url[count1]).get()
                val table = doc.select("table")[tb]
                val rows = table.select("tr")
                    val row = rows[count+1] //по номеру индекса получает строку
                    val cols = row.select("td")// разбиваем полученную строку по тегу  на столбы
                    val str1 = cols[1].text()
                    val Time_List = str1.split("|")
                    for (i in 0 until Time_List.size){
                        val str2 = Time_List[i]
                        listTime.add(Time(str2))
                    }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return listTime
        }


        override fun onPostExecute(result: MutableList<Time>) {
            //if you had a ui element, you could display the title
            adapter.set(result)
            progres.visibility = View.GONE
        }
    }
    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) {
                    title = TitleBlet
                    adapter = TimeAdapter()
                    TimeList.layoutManager = LinearLayoutManager(context)
                    TimeList.adapter = adapter
                    listTime.clear()
                    MyTask().execute()
                    Log.e("Internet Access", "Online Connect Intenet ")
                } else {
                    title = "Ожидание сети..."
                    Log.e("Internet Access", "Conectivity Failure !!! ")
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }

        private fun isOnline(context: Context): Boolean {
            return try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo
                //should check null because in airplane mode it will be null
                netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                false
            }

        }
    }

}
