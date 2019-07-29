package RaspBusMVP.View

import RaspBusMVP.Presenter.MainPresenter
import RaspBusMVP.Presenter.adapterRoute
import RaspBusMVP.route_Adapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.ConnectivityManager
import android.content.Context
import android.util.Log
import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.toxa.raspbusv2.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
//    companion object {
//        var pos= 0
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(NetworkChangeReceiver(), filter) //регистрация класса проверки состояния сети
    }


//    fun isOnline(): Boolean { //метод проверки состояния сети для изменения загаловка
//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val netInfo = cm.activeNetworkInfo
//        ConnectivityManager.CONNECTIVITY_ACTION
//        return netInfo != null && netInfo.isConnectedOrConnecting
//    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (isOnline(context)) { // если есть интернет соединение
                    title = "Расписаниерр" // изменить заголовок
                    MainPresenter().execute() // открытие потока заполнения данными
                    adapterRoute = route_Adapter() // установка адаптера списка
                    Route_list.layoutManager = LinearLayoutManager(context)
                    Route_list.adapter = adapterRoute // передача адаптера списка
                    progres.visibility = View.GONE // убираем прогресс бар
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
                //should check null because in airplane mode it will be null
                netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                false
            }

        }
    }


}




