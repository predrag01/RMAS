package elfak.mosis.underradar.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.Device
import elfak.mosis.underradar.databinding.FragmentAddDeviceBinding
import elfak.mosis.underradar.viewmodels.DeviceViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel


class AddDeviceFragment : Fragment() {

    private var _binding: FragmentAddDeviceBinding?=null
    private val userViewModel: UserViewModel by activityViewModels()
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private var selectedIndex=0
    private lateinit var selected:String
    private val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentAddDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addDeviceDropdownMenu.setSelection(selectedIndex)

        binding.addDeviceDropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedIndex=p2
                selected= p0!!.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.setSelection(selectedIndex)
            }
        }

        binding.addDeviceAdd.setOnClickListener {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view.windowToken, 0)

            if(checkInput())
            {
                var device: Device= Device(
                    title = binding.addDeviceEditTextTitle.text.toString(),
                    type = "radar",
                    description = binding.addDeviceEditTextDescription.text.toString(),
                    ownerId = userViewModel.user!!.id,
                    latitude = userViewModel.location!!.latitude,
                    longitude = userViewModel.location!!.longitude
                    )
                deviceViewModel.addDevice(device, userViewModel.user!!)
                findNavController().navigate(R.id.action_addDeviceFragment_to_homeFragment)
            }
        }

        binding.addDeviceCancle.setOnClickListener {
            findNavController().navigate(R.id.action_addDeviceFragment_to_homeFragment)
        }

    }

    private fun checkInput(): Boolean
    {
        var check=true
        if(binding.addDeviceEditTextTitle.text.isBlank()) {
            check = false
            binding.textInputTitle.error="Field cannot be empty"
        }
        if(binding.addDeviceEditTextDescription.text.isBlank())
        {
            check=false
            binding.textInputDescription.error="Field cannot be empty"
        }
        return check
    }
}