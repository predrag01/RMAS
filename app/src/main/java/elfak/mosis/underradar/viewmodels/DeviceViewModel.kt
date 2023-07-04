package elfak.mosis.underradar.viewmodels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import elfak.mosis.underradar.data.Device
import elfak.mosis.underradar.data.User

class DeviceViewModel : ViewModel() {

    private val database= Firebase.database.reference

    private val _device=MutableLiveData<Device?>(null)
    private val _devices=MutableLiveData<List<Device>>(emptyList())
    var device
        get() = _device.value
        set(value) { _device.value=value}

    var devices
        get()=_devices.value
        set(va){_devices.value=va}
    fun addDevice(device: Device, user: User)
    {
        database.child("Devices").child(device.id).setValue(device)
        database.child("Users").child(device.ownerId).child("points").setValue(user.points+10)
    }

    fun getDevices()
    {
        database.child("Devices").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val deviceList= mutableListOf<Device>()
                    for(dev in snapshot.children){
                        val d=dev.getValue(Device::class.java)
                        d?.let {
                            deviceList.add(d)
                        }
                        d?.let { Log.w(TAG, d.title) }
                    }
                    _devices.postValue(deviceList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException());
            }
        })
    }
}