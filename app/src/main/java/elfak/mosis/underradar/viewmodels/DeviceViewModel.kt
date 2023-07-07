package elfak.mosis.underradar.viewmodels

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.text.BoringLayout
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import elfak.mosis.underradar.data.Comment
import elfak.mosis.underradar.data.Device
import elfak.mosis.underradar.data.User
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt

class DeviceViewModel : ViewModel() {

    private val database= Firebase.database.reference

    private val _device=MutableLiveData<Device?>(null)
    private val _devices=MutableLiveData<List<Device>>(emptyList())
    private val _currentUserDevices=MutableLiveData<List<Device>>(emptyList())
    var device
        get() = _device.value
        set(value) { _device.value=value}

    var devices
        get()=_devices.value
        set(va){_devices.value=va}

    var currentUserDevices
        get()=_currentUserDevices.value
        set(va){_currentUserDevices.value=va}
    fun addDevice(device: Device, user: User)
    {
        database.child("Devices").child(device.id).setValue(device)
        database.child("Users").child(device.ownerId).child("points").setValue(user.points+10)
    }
    private fun getDistance(currentLat: Double, currentLon: Double, deviceLat: Double, deviceLon: Double): Double {
        val earthRadius = 6371000.0 // Earth's radius in meters

        val currentLatRad = Math.toRadians(currentLat)
        val deviceLatRad = Math.toRadians(deviceLat)
        val deltaLat = Math.toRadians(deviceLat - currentLat)
        val deltaLon = Math.toRadians(deviceLon - currentLon)

        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(currentLatRad) * cos(deviceLatRad) *
                sin(deltaLon / 2) * sin(deltaLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
    fun getDevices(location: LatLng, radius: Int=20, all: Boolean=true, camera: Boolean=false, radar: Boolean=false,
                   onDataLoaded: () -> Unit)
    {
        database.child("Devices").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val deviceList= mutableListOf<Device>()
                    for(dev in snapshot.children){
                        val d=dev.getValue(Device::class.java)
                        d?.let{
                            val distance=getDistance(location.latitude, location.longitude, d.latitude, d.longitude)
                            if(distance<=radius)
                            {
                                if(all)
                                {
                                    deviceList.add(d)
                                }
                                else if(camera)
                                {
                                    if(d?.type=="camera")
                                    {
                                        deviceList.add(d)
                                    }
                                }
                                else
                                {
                                    if(d?.type=="radar")
                                    {
                                        deviceList.add(d)
                                    }
                                }
                            }
                            }
                        }
                    devices=deviceList
                    onDataLoaded()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException());
            }
        })
    }

    fun like(user:User)
    {
        device!!.like=device!!.like+1
        database.child("Devices").child(device!!.id).child("like").setValue(device!!.like)
        database.child("Users").child(user.id).child("points").setValue(user.points+10)
    }

    fun dislike(user:User)
    {
        device!!.dislike=device!!.dislike+1
        database.child("Devices").child(device!!.id).child("dislike").setValue(device!!.dislike)
        database.child("Users").child(user.id).child("points").setValue(user.points+10)
    }

    fun getUserDevices(userId: String, onDataLoaded: () ->Unit)
    {
        database.child("Devices").orderByChild("ownerId").equalTo(userId)
            .addListenerForSingleValueEvent(object: ValueEventListener{
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