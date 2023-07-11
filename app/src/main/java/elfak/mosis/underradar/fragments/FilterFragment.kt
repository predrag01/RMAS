package elfak.mosis.underradar.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import elfak.mosis.underradar.R
import elfak.mosis.underradar.databinding.FragmentFilterBinding
import elfak.mosis.underradar.viewmodels.DeviceViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel

class FilterFragment : DialogFragment() {

    private  var _binding: FragmentFilterBinding?=null
    private val userViewModel: UserViewModel by activityViewModels()
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setContentView(R.layout.fragment_filter)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.CENTER)
        }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterCameraCheckbox.setOnClickListener {

            if(!binding.filterAllCheckbox.isChecked && !binding.filterRadarCheckbox.isChecked)
            {
                binding.filterCameraCheckbox.setChecked(true)
            }

            if(binding.filterAllCheckbox.isChecked)
            {
                binding.filterAllCheckbox.setChecked(false)
            }

            if(binding.filterRadarCheckbox.isChecked)
            {
                binding.filterRadarCheckbox.setChecked(false)
            }

            deviceViewModel.getDevices(location = userViewModel.location!!, camera = true){
                Toast.makeText(requireContext(), "Camera filtered", Toast.LENGTH_SHORT).show()
            }
        }

        binding.filterAllCheckbox.setOnClickListener {

            if(!binding.filterCameraCheckbox.isChecked && !binding.filterRadarCheckbox.isChecked)
            {
                binding.filterAllCheckbox.setChecked(true)
            }

            if(binding.filterCameraCheckbox.isChecked)
            {
                binding.filterCameraCheckbox.setChecked(false)
            }

            if(binding.filterRadarCheckbox.isChecked)
            {
                binding.filterRadarCheckbox.setChecked(false)
            }
            deviceViewModel.getDevices(location = userViewModel.location!!, all=true){
                Toast.makeText(requireContext(), "All filtered", Toast.LENGTH_SHORT).show()
            }
        }

        binding.filterRadarCheckbox.setOnClickListener {

            if(!binding.filterCameraCheckbox.isChecked && !binding.filterAllCheckbox.isChecked)
            {
                binding.filterRadarCheckbox.setChecked(true)
            }

            if(binding.filterCameraCheckbox.isChecked)
            {
                binding.filterCameraCheckbox.setChecked(false)
            }

            if(binding.filterAllCheckbox.isChecked)
            {
                binding.filterAllCheckbox.setChecked(false)
            }
            deviceViewModel.getDevices(location = userViewModel.location!!, radar = true){
                Toast.makeText(requireContext(), "Radar filtered", Toast.LENGTH_SHORT).show()
            }
        }
    }
}