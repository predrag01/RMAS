package elfak.mosis.underradar.viewmodels

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.underradar.adapters.UserAdapter
import elfak.mosis.underradar.data.User

class UserViewModel : ViewModel() {
    private val _user=MutableLiveData<User?>(null)
    private val _owner=MutableLiveData<User?>(null)
    private val _users= MutableLiveData<List<User>>(emptyList())
    private val _location=MutableLiveData<LatLng?>(null)
    private val database=Firebase.database.reference
    fun getMutable()=_user

    var user
        get()=_user.value
        set(value){_user.value=value}

    var owner
        get()=_owner.value
        set(value){_owner.value=value}

    fun getMutableLocation()=_location

    var location
        get()=_location.value
        set(value){_location.value=value}

    var users
        get()=_users.value
        set(value){ _users.value=value}
    fun getUsers(onDataLoaded: () -> Unit)
    {
        database.child("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val userList= mutableListOf<User>()
                    for(user in snapshot.children)
                    {
                        val u=user.getValue(User::class.java)
                        userList.add(u!!)
                    }
                    users=userList.sortedByDescending { it.points }

                    onDataLoaded()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException());
            }
        })
    }

    fun getOwner(ownerId:String)
    {
        database.child("Users").child(ownerId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                owner=snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException());
            }
        })
    }
}