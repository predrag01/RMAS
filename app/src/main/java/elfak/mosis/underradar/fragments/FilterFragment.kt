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
import elfak.mosis.underradar.viewmodels.LoggedUserViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel

class FilterFragment : DialogFragment() {

    private  var _binding: FragmentFilterBinding?=null
    private val loggedUserViewModel: LoggedUserViewModel by activityViewModels()
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private var radius: Int = 10000
    private var all:Boolean=true
    private var camera:Boolean=false
    private var radar:Boolean=false
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

        if(deviceViewModel.all)
        {
            binding.filterAllCheckbox.isChecked=true
            binding.filterCameraCheckbox.isChecked=false
            binding.filterRadarCheckbox.isChecked=false
            all=true
            camera=false
            radar=false
        }
        else if(deviceViewModel.camera)
        {
            binding.filterCameraCheckbox.isChecked=true
            binding.filterAllCheckbox.isChecked=false
            binding.filterRadarCheckbox.isChecked=false
            all=false
            camera=true
            radar=false
        }
        else
        {
            binding.filterRadarCheckbox.isChecked=true
            binding.filterAllCheckbox.isChecked=false
            binding.filterCameraCheckbox.isChecked=false
            all=false
            camera=false
            radar=true
        }


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

            all=false
            camera=true
            radar=false

            deviceViewModel.filterLocations(rad=radius, all = this.all, camera = this.camera, radar = this.radar,
                loc = loggedUserViewModel.location!!)

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

            all=true
            camera=false
            radar=false

            deviceViewModel.filterLocations(rad=radius, all = this.all, camera = this.camera, radar = this.radar,
                loc = loggedUserViewModel.location!!)

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

            all=false
            camera=false
            radar=true

            deviceViewModel.filterLocations(rad=radius, all = this.all, camera = this.camera, radar = this.radar,
                loc = loggedUserViewModel.location!!)

        }

        binding.slider.addOnChangeListener { _, value, _ ->
            radius = value.toInt() * 1000
            deviceViewModel.filterLocations(all = this.all, camera = this.camera, radar = this.radar,
                loc = loggedUserViewModel.location!!, rad=radius)
        }
    }
}