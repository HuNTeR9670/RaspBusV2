package com.toxa.RaspBusMVP.View

import com.toxa.RaspBusMVP.Presenter.MainPresenter
import com.toxa.RaspBusMVP.Presenter.adapterRoute
import com.toxa.RaspBusMVP.route_Adapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.ConnectivityManager
import android.content.Context
import android.util.Log
import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.os.AsyncTask
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.toxa.RaspBusMVP.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter) //регистрация класса проверки состояния сети
        super.onStart()
    }
    override fun onDestroy() {
        MainPresenter().cancel(false) // завершение асинхронной задачи
        NetworkChangeReceiver().abortBroadcast // остановка вещателя
        super.onDestroy()
    }


     inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) { // если есть интернет соединение
                    pro.visibility = View.GONE // убираем прогрес бар
                    title = "Расписание" // изменить заголовок
                    MainPresenter().execute() // открытие потока заполнения данными
                    adapterRoute = route_Adapter() // установка адаптера списка
                    Route_list.layoutManager = LinearLayoutManager(context) // назначаем разметку
                    Route_list.adapter = adapterRoute // передача адаптера списка
                    Log.e("Check", "Online Connect Internet ")  // пишем в логи присутсвие интернета
                } else { // если нет интернет соединения
                    title = "Ожидание сети..." // изменить заголовок
                    Log.e("Check", "No Online Connect !!! ") // пишем в логи отсутсвие интернета
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }

        private fun isOnline(context: Context): Boolean { // проверка состояния сети
            return try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo
                //проверка на включёность режима "в полёте"
                netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                false
            }

        }
    }


}




