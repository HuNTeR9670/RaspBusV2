package com.toxa.RaspBusMVP.View

import com.toxa.RaspBusMVP.Presenter.StopPresenter
import com.toxa.RaspBusMVP.Presenter.adapterStop
import com.toxa.RaspBusMVP.StopAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_stop.*
import android.view.View
import com.toxa.RaspBusMVP.R


private var TitleStop= ""

class StopActivity : AppCompatActivity() {

    companion object {
        const val pos = "total_count"
        const val PrevTitle = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop)
        title = intent.getStringExtra(PrevTitle) // получаем наименование маршрута и устанавливает в качестве заголовка
        TitleStop = intent.getStringExtra(PrevTitle) // получаем наименование маршрута
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter) //регистрация класса проверки состояния сети
        val actionBar = supportActionBar // добавляем кнопку "Назад"
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
            }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // обработка нажатия кнопки "Назад"
        this.finish()
        return true
    }

//    private fun isOnline(): Boolean {
//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val netInfo = cm.activeNetworkInfo
//        return netInfo != null && netInfo.isConnectedOrConnecting
//    }



    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) { // если есть интернет соединение
                    title = TitleStop
                    StopPresenter().execute()
                    adapterStop = StopAdapter()
                    Stop_List.layoutManager = LinearLayoutManager(context)
                    Stop_List.adapter = adapterStop
                    progres.visibility = View.GONE
                    if (adapterStop.itemCount==0){ // если выходной день и список пустой
                        eror.visibility = View.VISIBLE}
                    if (adapterStop.itemCount==0 && !isOnline(context)){
                        eror.visibility = View.GONE}
                    Log.e("Check", "Online Connect Internet ")
                } else {
                    title = "Ожидание сети..."
                    Log.e("Check", "No Online Connect !!! ")
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

