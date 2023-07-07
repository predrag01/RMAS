package elfak.mosis.underradar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import elfak.mosis.underradar.R
import elfak.mosis.underradar.databinding.FragmentFilterBinding

class FilterFragment : DialogFragment() {

    private  var _binding: FragmentFilterBinding?=null

    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentFilterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var value=2
        binding.filterEditTextRadius.setText(value.toString())

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
        }
    }
}