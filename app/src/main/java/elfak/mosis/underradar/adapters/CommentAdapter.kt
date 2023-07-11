package elfak.mosis.underradar.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        if(comments.get(position).profileImg!=null)
        {
            Glide.with(context)
                .load(comments.get(position).profileImg)
                .into(holder.comment_profile_image_view!!)
        }
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        var txt_comment_name: TextView?=null
        var txt_comment: TextView?=null
        var comment_profile_image_view: ImageView?=null

        init {
            txt_comment=itemView.findViewById(R.id.txt_comment) as TextView
            txt_comment_name=itemView.findViewById(R.id.txt_comment_name) as TextView
            comment_profile_image_view=itemView.findViewById(R.id.comment_profile_image_view) as ImageView
        }
    }
}