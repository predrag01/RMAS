package elfak.mosis.underradar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.Comment


class CommentAdapter(internal var context: Context,
                     internal val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_comment_name!!.text=comments.get(position).username
        holder.txt_comment!!.text=comments.get(position).comment
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        var txt_comment_name: TextView?=null
        var txt_comment: TextView?=null

        init {
            txt_comment=itemView.findViewById(R.id.txt_comment) as TextView
            txt_comment_name=itemView.findViewById(R.id.txt_comment_name) as TextView

        }
    }
}