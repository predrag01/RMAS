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
    private val _owner=MutableLiveData<User?>(null)
    private val database=Firebase.database.reference

    var owner
        get()=_owner.value
        set(value){_owner.value=value}

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