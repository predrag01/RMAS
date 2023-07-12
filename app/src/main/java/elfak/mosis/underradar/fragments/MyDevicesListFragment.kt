package elfak.mosis.underradar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import elfak.mosis.underradar.R
import elfak.mosis.underradar.adapters.DeviceAdapter
import elfak.mosis.underradar.databinding.FragmentMyDevicesListBinding
import elfak.mosis.underradar.viewmodels.DeviceViewModel
import elfak.mosis.underradar.viewmodels.DevicesViewModel
import elfak.mosis.underradar.viewmodels.LoggedUserViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel

class MyDevicesListFragment : Fragment() {

    private var _binding: FragmentMyDevicesListBinding?=null
    private val devicesViewModel: DevicesViewModel by activityViewModels()
    private val loggedUserViewModel: LoggedUserViewModel by activityViewModels()
    private lateinit var deviceAdapter: DeviceAdapter

    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMyDevicesListBinding.inflate(inflater, container, false)
        devicesViewModel.getUserDevices(userId = loggedUserViewModel.user!!.id){
            deviceAdapter= DeviceAdapter(requireContext(), devicesViewModel.currentUserDevices!!)
            binding.myDevicesList.adapter=deviceAdapter
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        devicesViewModel.currentUserDevices=null
    }

    override fun onDestroy() {
        super.onDestroy()
        devicesViewModel.currentUserDevices=null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        devicesViewModel.currentUserDevices=null
    }
}