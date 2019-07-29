package View

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.toxa.raspbusv2.R
import kotlinx.android.synthetic.main.route_item.view.*
import model.Route

class routeHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(route: Route) {
        itemView.Route_Name.text = route.route_name
        Picasso.get().load(route.img).into(itemView.image_view)
    }

}