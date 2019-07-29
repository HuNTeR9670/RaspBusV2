import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import View.StopActivity
import com.toxa.raspbusv2.R
import kotlinx.android.synthetic.main.route_item.view.*
import model.Route

class route_Adapter: RecyclerView.Adapter<routeHolder>() {
    private val listRoute = mutableListOf<Route>()
    companion object {
        var pos= 0
    }
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): routeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.route_item,parent,false)
        return routeHolder(view)
    }

    override fun onBindViewHolder(holder: routeHolder, position: Int) {
        holder.bind( listRoute[position])
        holder.itemView.card_view.setOnClickListener{
            context = it.context
            val intent = Intent(context, StopActivity::class.java)
            intent.putExtra(StopActivity.pos, holder.adapterPosition)
            intent.putExtra(StopActivity.title1, holder.itemView.Route_Name.text)
            pos =position
            it.context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = listRoute.size

    fun set(list: MutableList<Route>){
        this.listRoute.clear()
        this.listRoute.addAll(list)
        notifyDataSetChanged()
    }
}