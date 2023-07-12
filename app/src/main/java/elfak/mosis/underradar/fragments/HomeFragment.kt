package elfak.mosis.underradar.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.Device
import elfak.mosis.underradar.databinding.FragmentHomeBinding
import elfak.mosis.underradar.viewmodels.DeviceViewModel
import elfak.mosis.underradar.viewmodels.LoggedUserViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentHomeBinding?=null
    private val userViewModel: UserViewModel by activityViewModels()
    private val loggedUserViewModel: LoggedUserViewModel by activityViewModels()
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private var devicesMap: MutableMap<Marker?, Device> = mutableMapOf()
    private var map: GoogleMap? =null
    private lateinit var location: MutableLiveData<Location>
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentHomeBinding.inflate(inflater, container, false)
        deviceViewModel.device=null
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment=childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        location= MutableLiveData()

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(requireActivity())

        mapFragment!!.getMapAsync{ mMap ->
            map =mMap
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isCompassEnabled = true

            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1001)
                return@getMapAsync
            }
            mMap.isMyLocationEnabled=true

            fusedLocationClient.lastLocation.addOnCompleteListener {location->
                if(location.result  !=null)
                {
                    lastLocation=location.result
                    val currentLatLong= LatLng(location.result.latitude, location.result.longitude)
                    loggedUserViewModel.location=currentLatLong

                    val googlePlex = CameraPosition.builder()
                        .target(currentLatLong)
                        .zoom(15f)
                        .bearing(0f)
                        .tilt(0f)
                        .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null)

                    if(deviceViewModel.devices!=null)
                    {
                        deviceViewModel.getDevices(location = LatLng(lastLocation.latitude, lastLocation.longitude),
                            onDataLoaded = {
                                setUpMarkers()
                            })
                    }
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
                    loggedUserViewModel.location=LatLng(lastLocation.latitude, lastLocation.longitude)
                    findNavController().navigate(R.id.action_homeFragment_to_addDeviceFragment)
                }
            }

        }

        binding.profButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rangListFragment)
        }

        binding.filterButton.setOnClickListener {
            fusedLocationClient.lastLocation.addOnCompleteListener {location->
                if(location.result  !=null)
                {
                    lastLocation=location.result
                }
            }
            loggedUserViewModel.location=LatLng(lastLocation.latitude, lastLocation.longitude)
            val filterDialog= FilterFragment()
            filterDialog.show(requireActivity().supportFragmentManager, "showFilterDialog")
        }

        deviceViewModel.devices.observe(viewLifecycleOwner) { devices ->
            setUpMarkers()
        }
    }

    override fun onResume() {
        super.onResume()
        setUpMarkers()
    }

    private fun setUpMarkers()
    {
        map?.clear()
        devicesMap= mutableMapOf()
        val devices = deviceViewModel.devices.value
        if(devices!=null)
        {
            for(device in devices)
            {
                val marker=map?.addMarker(MarkerOptions().position(LatLng(device.latitude, device.longitude)).title(device.type))
                devicesMap[marker] = device
            }
        }

        map?.setOnMarkerClickListener { marker ->

            if(devicesMap.contains(marker))
            {
                deviceViewModel.device=devicesMap[marker]
                userViewModel.getOwner(deviceViewModel.device!!.ownerId, onSuccess = {
                    findNavController().navigate(R.id.action_homeFragment_to_deviceDetailsFragment)
                })
            }
            else
            {
                Toast.makeText(context, "Impossible access", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

}