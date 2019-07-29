import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.toxa.raspbus.model.Stop
import kotlinx.android.synthetic.main.stop_item.view.*


class StopView(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bind(stop: Stop) {
        itemView.name_stop.text = stop.stop_name

        }

    }

