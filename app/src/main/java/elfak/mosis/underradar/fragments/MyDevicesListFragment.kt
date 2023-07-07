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
import elfak.mosis.underradar.viewmodels.UserViewModel

class MyDevicesListFragment : Fragment() {

    private var _binding: FragmentMyDevicesListBinding?=null
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var deviceAdapter: DeviceAdapter

    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMyDevicesListBinding.inflate(inflater, container, false)
        deviceViewModel.getUserDevices(userId = userViewModel.user!!.id){
            deviceAdapter= DeviceAdapter(requireContext(), deviceViewModel.currentUserDevices!!)
            binding.myDevicesList.adapter=deviceAdapter
        }
        return binding.root
    }
}