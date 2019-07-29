package RaspBusMVP.View

import RaspBusMVP.StopAdapter
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.toxa.raspbus.model.Stop
import kotlinx.android.synthetic.main.activity_stop.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*
import android.view.View



private val listRoute = mutableListOf<Stop>()
private lateinit var adapter: StopAdapter

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

class StopActivity : AppCompatActivity() {

    companion object {

        const val pos = "total_count"
        const val title1 = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.toxa.raspbusv2.R.layout.activity_stop)
        title = intent.getStringExtra(title1)
        TitleBlet = intent.getStringExtra(title1)
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter)
        val actionBar = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
            }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return true
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }


    @SuppressLint("StaticFieldLeak")
    inner class MyTask : AsyncTask<Void, Void, MutableList<Stop>>() {

        override fun doInBackground(vararg params: Void): MutableList<Stop> {
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
            try {
                doc = Jsoup.connect(Url[count]).get()
                val table = doc.select("table")[tb]
                val rows = table.select("tr")
                var j = 0
                if (count==3){j=3}
                else {j = rows.size}
                for (i in 1 until j) {
                    val row = rows[i] //по номеру индекса получает строку
                    val cols = row.select("td")// разбиваем полученную строку по тегу  на столбы
                    val str1 = cols[0].text()
                    if (str1=="—"){}else
                    listRoute.add(Stop(str1))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return listRoute
        }


        override fun onPostExecute(result: MutableList<Stop>) {
            //if you had a ui element, you could display the title
            adapter.set(result)
            if (result.size==0){
                eror.visibility = View.VISIBLE}
            if (result.size==0 && !isOnline()){
                eror.visibility = View.GONE}
            progres.visibility = View.GONE
        }
    }
    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) {
                    title = TitleBlet
                    MyTask().execute()
                    adapter = StopAdapter()
                    Stop_List.layoutManager = LinearLayoutManager(context)
                    Stop_List.adapter = adapter
                    listRoute.clear()
                    Log.e("keshav", "Online Connect Intenet ")
                } else {
                    title = "Ожидание сети..."
                    Log.e("keshav", "Conectivity Failure !!! ")
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

