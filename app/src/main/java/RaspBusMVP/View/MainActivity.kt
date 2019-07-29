package RaspBusMVP.View

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import RaspBusMVP.model.Route
import RaspBusMVP.route_Adapter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import android.net.ConnectivityManager
import android.content.Context
import android.util.Log
import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver


private val listRoute = mutableListOf<Route>()
private lateinit var adapter: route_Adapter
val array =
arrayOf("АВТОВОКЗАЛ-СЕЛЬХОЗТЕХНИКА","БОЛЬНИЦА (Ксты)-Ж/Д БОЛЬНИЦА (Новка)",
"БОЛЬНИЦА (Ксты)-Ж/Д БОЛЬНИЦА (Новка)","АВТОВОКЗАЛ-ЭЛЕКТРОСЕТИ",
"МАРИНЕНКО-БОРОВУХА-2","МАРИНЕНКО-БОРОВУХА-2","АВТОВОКЗАЛ-КАЛИНИНА","АВТОВОКЗАЛ-ГВАРДЕЕЦ",
"АВТОВОКЗАЛ-БАБУШКИНА","АВТОВОКЗАЛ-КСТЫ","АВТОВОКЗАЛ-ТРОСНИЦКАЯ","АВТОВОКЗАЛ-ГОРТОП",
"Мариненко-Ксты","АВТОВОКЗАЛ-КСТЫ ч/з Аэродром","МАРИНЕНКО-АЭРОДРОМ-АВТОВОКЗАЛ",
"АВТОВОКЗАЛ-АЭРОДРОМ","МАРИНЕНКО-АВТОВОКЗАЛ-АЭРОДРОМ","МАРИНЕНКО-АВТОВОКЗАЛ-АЭРОДРОМ")

class MainActivity : AppCompatActivity() {
    companion object {
        var pos= 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.toxa.raspbusv2.R.layout.activity_main)
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter)
    }




    fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        ConnectivityManager.CONNECTIVITY_ACTION
        return netInfo != null && netInfo.isConnectedOrConnecting
    }


   inner class MyTask : AsyncTask<Void, Void, MutableList<Route>> () {

       override fun onPreExecute() {
           if (!isOnline()){
               title="Ожидание сети.."
           }
           super.onPreExecute()
       }

        override fun doInBackground(vararg params: Void): MutableList<Route> {
            val doc: Document
            val list: ArrayList<String> = ArrayList()
            try {
                doc = Jsoup.connect("http://ap2polotsk.of.by/ap2/rasp/gorod/").get()
                val pngs = doc.select("img[src$=.png]")
                for (i in 0 until pngs.size ){
                    listRoute.add(
                        Route(
                            array[i],
                            pngs.eq(i).attr("src")
                        )
                    )
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return listRoute
        }


        override fun onPostExecute(result: MutableList<Route>) {
            //if you had a ui element, you could display the title
            adapter.set(result)
            progres.visibility = GONE
        }


}

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) {
                    title = "Расписание"
                    MyTask().execute()
                    adapter = route_Adapter()
                    Route_list.layoutManager = LinearLayoutManager(context)
                    Route_list.adapter = adapter
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




