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
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter) // регистрируем класс проверки состояния сети
        val actionBar = supportActionBar // добавляем кнопку "назад"
        actionBar!!.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(TitleStop)
        TitleTime = intent.getStringExtra(TitleStop)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return true
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) {
                    title = TitleTime
                    adapterTime = TimeAdapter()
                    TimeList.layoutManager = LinearLayoutManager(context)
                    TimeList.adapter = adapterTime
                    TimePresenter().execute()
                    pro.visibility = View.GONE
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
                //проверка на включённость режима "в полёте"
                netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                false
            }

        }
    }

}
