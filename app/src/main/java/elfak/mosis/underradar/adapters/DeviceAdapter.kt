package elfak.mosis.underradar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.Device
import elfak.mosis.underradar.data.User

class DeviceAdapter (internal var context: Context, internal val devices: List<Device>) : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_device_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txt_my_device_list_title!!.text=devices.get(position).title
        holder.txt_my_device_list_type!!.text=devices.get(position).type
        holder.txt_my_device_list_description!!.text=devices.get(position).description
        holder.txt_my_device_list_no!!.text= (position+1).toString()

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var txt_my_device_list_title: TextView?=null
        var txt_my_device_list_type: TextView?=null
        var txt_my_device_list_description: TextView?=null
        var txt_my_device_list_no: TextView?=null

        init {
            txt_my_device_list_title=itemView.findViewById(R.id.txt_my_device_list_title) as TextView
            txt_my_device_list_type=itemView.findViewById(R.id.txt_my_device_list_type) as TextView
            txt_my_device_list_description=itemView.findViewById(R.id.txt_my_device_list_description) as TextView
            txt_my_device_list_no=itemView.findViewById(R.id.txt_my_device_list_no) as TextView
        }
    }
}