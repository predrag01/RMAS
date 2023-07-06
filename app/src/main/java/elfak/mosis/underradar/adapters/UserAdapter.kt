package elfak.mosis.underradar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.Comment
import elfak.mosis.underradar.data.User

class UserAdapter(internal var context: Context,
                  internal val users: List<User>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rang_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt_rang_list_username!!.text=users.get(position).userName
        holder.txt_rang_list_name!!.text=users.get(position).name
        holder.txt_rang_list_last_name!!.text=users.get(position).lastName
        holder.txt_rang_list_point_value!!.text= users.get(position).points.toString()
        holder.txt_rang_list_no!!.text= (position+1).toString()

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var txt_rang_list_username: TextView?=null
        var txt_rang_list_name: TextView?=null
        var txt_rang_list_last_name: TextView?=null
        var txt_rang_list_point_value: TextView?=null
        var txt_rang_list_no: TextView?=null

        init {
            txt_rang_list_username=itemView.findViewById(R.id.txt_rang_list_username) as TextView
            txt_rang_list_name=itemView.findViewById(R.id.txt_rang_list_name) as TextView
            txt_rang_list_last_name=itemView.findViewById(R.id.txt_rang_list_last_name) as TextView
            txt_rang_list_point_value=itemView.findViewById(R.id.txt_rang_list_point_value) as TextView
            txt_rang_list_no=itemView.findViewById(R.id.txt_rang_list_no) as TextView
        }
    }
}