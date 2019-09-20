package com.toxa.RaspBusMVP.View

import com.toxa.RaspBusMVP.TimeAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.toxa.RaspBusMVP.Presenter.TimePresenter
import com.toxa.RaspBusMVP.Presenter.adapterTime
import com.toxa.RaspBusMVP.R
import kotlinx.android.synthetic.main.activity_time.*


private var TitleTime = ""


class TimeActivity : AppCompatActivity() {

    companion object {
        const val TitleStop = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        val actionBar = supportActionBar // добавляем кнопку "назад"
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(TitleStop) // получаем наименование остановки и устанавливает в качестве заголовка
        TitleTime = intent.getStringExtra(TitleStop) // получаем наименование остановки и устанавливает в качестве заголовка

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
        TimePresenter().cancel(false) // завершение асинхронной задачи
        NetworkChangeReceiver().abortBroadcast // остановка вещателя
        super.onDestroy()
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) { //если есть интернет соеденение
                    title = TitleTime // меняем заголовок
                    adapterTime = TimeAdapter() // устанавливаем адаптер
                    TimeList.layoutManager = LinearLayoutManager(context) // назначаем разметку
                    TimeList.adapter = adapterTime // передача адаптера списка
                    TimePresenter().execute() // открытие потока заполнения данными
                    pro.visibility = View.GONE //убираем прогресс бар
                    Log.e("Internet Access", "Online Connect Internet ") // пишем в логи присутсвие интернета
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
                //проверка на включённость режима "в полёте"
                netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                false
            }

        }
    }

}
