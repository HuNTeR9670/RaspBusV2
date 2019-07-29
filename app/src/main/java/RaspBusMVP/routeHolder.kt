package RaspBusMVP

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.route_item.view.*
import RaspBusMVP.model.Route

class routeHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(route: Route) {
        itemView.Route_Name.text = route.route_name
        Picasso.get().load(route.img).into(itemView.image_view)
    }

}