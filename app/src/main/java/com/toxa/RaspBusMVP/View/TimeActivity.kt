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
import com.toxa.RaspBusMVP.Presenter.TimePresenter
import com.toxa.RaspBusMVP.Presenter.adapterTime
import com.toxa.RaspBusMVP.R
import com.toxa.RaspBusMVP.model.Time
import kotlinx.android.synthetic.main.activity_time.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*




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

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) {
                    title = TitleBlet
                    adapterTime = TimeAdapter()
                    TimeList.layoutManager = LinearLayoutManager(context)
                    TimeList.adapter = adapterTime
                    TimePresenter().execute()
                    progres.visibility = View.GONE
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
