package elfak.mosis.underradar.fragments

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.Device
import elfak.mosis.underradar.databinding.FragmentHomeBinding
import elfak.mosis.underradar.viewmodels.DeviceViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentHomeBinding?=null
    private val userViewModel: UserViewModel by activityViewModels()
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private lateinit var database: DatabaseReference
    private val devicesMap: MutableMap<Marker?, Device> = mutableMapOf()
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentHomeBinding.inflate(inflater, container, false)
        database=Firebase.database.reference
        deviceViewModel.device=null
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment=childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(requireActivity())

        mapFragment!!.getMapAsync{ mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isCompassEnabled = true

            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                return@getMapAsync
            }
            mMap.isMyLocationEnabled=true


            fusedLocationClient.lastLocation.addOnCompleteListener {location->
                if(location.result  !=null)
                {
                    lastLocation=location.result
                    val currentLatLong= LatLng(location.result.latitude, location.result.longitude)

                    val googlePlex = CameraPosition.builder()
                        .target(currentLatLong)
                        .zoom(15f)
                        .bearing(0f)
                        .tilt(0f)
                        .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 900, null)

                    setUpMarkers(mMap)
                }
            }.addOnFailureListener{
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }


        }

        binding.addButton.setOnClickListener{
            fusedLocationClient.lastLocation.addOnCompleteListener {location->
                if(location.result  !=null)
                {
                    lastLocation=location.result
                    userViewModel.location=LatLng(location.result.latitude, location.result.longitude)
                    Toast.makeText(context, lastLocation.longitude.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            userViewModel.location=LatLng(lastLocation.latitude, lastLocation.longitude)
            findNavController().navigate(R.id.action_homeFragment_to_addDeviceFragment)
        }

        binding.profButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rangListFragment)
        }

        binding.filterButton.setOnClickListener {
            val filterDialog= FilterFragment()
            filterDialog.show(requireActivity().supportFragmentManager, "showFilterDialog")
        }
    }

    /*override fun onResume() {
        super.onResume()

        val filterFragment = childFragmentManager.findFragmentByTag("showFilterDialog") as? FilterFragment
        filterFragment?.dialog?.setOnDismissListener {

        }
    }*/

    private fun setUpMarkers(map: GoogleMap)
    {
        deviceViewModel.getDevices(location = LatLng(lastLocation.latitude, lastLocation.longitude)){
            if(deviceViewModel.devices!=null)
            {
                for(device in deviceViewModel.devices!!)
                {
                    val marker=map.addMarker(MarkerOptions().position(LatLng(device.latitude, device.longitude)).title(device.type))
                    devicesMap[marker] = device
                }
            }
        }


        /*database.child("Devices").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    devicesMap.clear()
                    val deviceList= mutableListOf<Device>()
                    for(dev in snapshot.children){
                        val d=dev.getValue(Device::class.java)
                        d?.let {
                            deviceList.add(d)
                            val marker=map.addMarker(MarkerOptions().position(LatLng(d.latitude, d.longitude)).title(d.type))
                            devicesMap[marker] = d
                        }
                    }
                    deviceViewModel.devices=deviceList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException());
            }
        })*/


        map.setOnMarkerClickListener { marker ->

            if(devicesMap.contains(marker))
            {
                deviceViewModel.device=devicesMap[marker]
                findNavController().navigate(R.id.action_homeFragment_to_deviceDetailsFragment)
            }
            else
            {
                Toast.makeText(context, "Ne radi", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

}