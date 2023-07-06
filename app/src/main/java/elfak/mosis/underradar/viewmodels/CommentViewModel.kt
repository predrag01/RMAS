package elfak.mosis.underradar.viewmodels

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.underradar.data.Comment
import elfak.mosis.underradar.data.Device
import elfak.mosis.underradar.data.User

class CommentViewModel : ViewModel() {
    private val database= Firebase.database.reference

    private val _comment= MutableLiveData<Comment?>(null)
    private val _comments= MutableLiveData<List<Comment>>(emptyList())
    var comment
        get() = _comment.value
        set(value) { _comment.value=value}

    var comments
        get()=_comments.value
        set(va){_comments.value=va}

    fun addComment(comm: String, dev: String, user: User)
    {
        var comment: Comment=Comment(ownerId = user.id, deviceId = dev, comment = comm, username = user.userName)
        database.child("Comments").child(comment.id).setValue(comment)
        database.child("Users").child(user.id).child("points").setValue(user.points+10)
    }

    fun getCommentsForDevice(deviceId: String)
    {
        database.child("Comments").orderByChild("deviceId").equalTo(deviceId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val comm = mutableListOf<Comment>()

                    for (commentSnapshot in snapshot.children) {
                        val comment = commentSnapshot.getValue(Comment::class.java)
                        comment?.let { comm.add(it) }
                    }
                    comments = comm
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Failed to load comments: ${error.message}")
                }
            })
    }
}