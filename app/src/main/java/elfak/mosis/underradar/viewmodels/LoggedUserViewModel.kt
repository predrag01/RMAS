package elfak.mosis.underradar.viewmodels

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.underradar.data.User
import java.util.UUID

class LoggedUserViewModel: ViewModel() {
    private val _user= MutableLiveData<User?>(null)
    private val _location= MutableLiveData<LatLng?>(null)
    private val database= Firebase.database.reference
    private val firebaseAuth=FirebaseAuth.getInstance()
    private val storageRef= FirebaseStorage.getInstance().reference

    var user
        get()=_user.value
        set(value){_user.value=value}

    var location
        get()=_location.value
        set(value){_location.value=value}

    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            database.child("Users").child(it.user!!.uid).addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(user==null)
                    {
                        user=snapshot.getValue(User::class.java)
                        onSuccess()
                    }
                    else{
                        user=snapshot.getValue(User::class.java)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
        }.addOnFailureListener {
            onFailure()
            Log.w(ContentValues.TAG, "Failed to read value.")
        }
    }

    fun register(user: User, password: String, imageURI: Uri?, onSuccess: () -> Unit, onFailure: ()-> Unit)
    {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { it ->
                user.id=it.user!!.uid
                if(imageURI!==null)
                {
                    val uuid= UUID.randomUUID().toString()+".jpg"
                    storageRef.child("Profile photo").child(uuid).putFile(imageURI!!).addOnSuccessListener {
                        storageRef.child("Profile photo").child(uuid).downloadUrl.addOnSuccessListener {uri->
                            user.imageURL=uri.toString()
                            database.child("Users").child(user.id).setValue(user).addOnSuccessListener {
                                if(this.user==null)
                                {
                                    this.user=user
                                    onSuccess()
                                }
                                else
                                {
                                    this.user=user
                                }
                            }.addOnFailureListener {
                                onFailure()
                                Log.w(ContentValues.TAG, it.toString())
                            }
                        }.addOnFailureListener{
                            onFailure()
                            Log.w(ContentValues.TAG, it.toString())
                        }
                    }.addOnFailureListener{
                        onFailure()
                        Log.w(ContentValues.TAG, it.toString())
                    }
                }
                else
                {
                    database.child("Users").child(user.id).setValue(user).addOnSuccessListener {
                        this.user=user
                        onSuccess()
                    }.addOnFailureListener {
                        onFailure()
                        Log.w(ContentValues.TAG, it.toString())
                    }
                }
            }
            .addOnFailureListener{
                onFailure()
                Log.w(ContentValues.TAG, it.toString())
            }
    }
}