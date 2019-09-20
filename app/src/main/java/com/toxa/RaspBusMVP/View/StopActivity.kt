package com.toxa.RaspBusMVP.View

import android.annotation.SuppressLint
import com.toxa.RaspBusMVP.StopAdapter
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
import kotlinx.android.synthetic.main.activity_stop.*
import android.view.View
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import com.toxa.RaspBusMVP.Presenter.*
import com.toxa.RaspBusMVP.R


private var TitleStop= ""

class StopActivity : AppCompatActivity() {

    companion object {
        const val PrevTitle = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop)
        title = intent.getStringExtra(PrevTitle) // получаем наименование маршрута и устанавливает в качестве заголовка
        TitleStop = intent.getStringExtra(PrevTitle) // получаем наименование маршрута
        val actionBar = supportActionBar // добавляем кнопку "Назад"
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // обработка нажатия кнопки "Назад"
        this.finish() // завершаем текущее активити
        return true
    }

    override fun onStart() {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter) //регистрация класса проверки состояния сети
        super.onStart()
    }
    override fun onDestroy() {
        StopPresenter().cancel(false) // завершение асинхронной задачи
        NetworkChangeReceiver().abortBroadcast // остановка вещателя
        super.onDestroy()
    }



    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) { // если есть интернет соединение
                    title = TitleStop //меняем заголовок
                    adapterStop = StopAdapter() // устанавливаем адаптер
                    StopPresenter().execute() // открытие потока заполнения данными
                    Stop_List.layoutManager = LinearLayoutManager(context) // назначем разметку
                    Stop_List.adapter = adapterStop // передача адаптера списка
                    pro.visibility = View.GONE //убираем прогресс бар
                    Log.e("Check", "Online Connect Internet ") // пишем в логи присутсвие интернета
                } else { // если нет интернет соеденения
                    title = "Ожидание сети..." // меняем заголовок
                    Log.e("Check", "No Online Connect !!! ")// пишем в логи отсутсвие интернета
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }

        private fun isOnline(context: Context): Boolean { // проверка состояния сети
            return try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo
                //проверка на включенность режима "в полёте"
                netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                false
            }

        }
    }
}

