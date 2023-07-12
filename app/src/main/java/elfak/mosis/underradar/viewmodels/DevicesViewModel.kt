package elfak.mosis.underradar.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import elfak.mosis.underradar.data.Device

class DevicesViewModel: ViewModel() {

    private val database= Firebase.database.reference
    private val _currentUserDevices= MutableLiveData<List<Device>>(emptyList())

    var currentUserDevices
        get()=_currentUserDevices.value
        set(va){_currentUserDevices.value=va}

    fun getUserDevices(userId: String, onDataLoaded: () ->Unit)
    {
        database.child("Devices").orderByChild("ownerId").equalTo(userId)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        val devicesList= mutableListOf<Device>()
                        for(device in snapshot.children)
                        {
                            val dev=device.getValue(Device::class.java)
                            dev?.let { devicesList.add(it) }
                        }
                        currentUserDevices=devicesList
                        onDataLoaded()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Failed to load comments: ${error.message}")
                }

            })
    }
}