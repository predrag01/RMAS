package elfak.mosis.underradar.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import elfak.mosis.underradar.R

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootview=inflater.inflate(R.layout.fragment_home, container, false)
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
                        .tilt(45f)
                        .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null)

                }
            }.addOnFailureListener{
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        return rootview
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Customize map settings if needed
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true

        val addButton = requireView().findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addDeviceFragment)
        }
    }

}