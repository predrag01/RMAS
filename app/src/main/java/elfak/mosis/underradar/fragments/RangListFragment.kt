package elfak.mosis.underradar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import elfak.mosis.underradar.R
import elfak.mosis.underradar.adapters.UserAdapter
import elfak.mosis.underradar.databinding.FragmentRangListBinding
import elfak.mosis.underradar.viewmodels.UserViewModel

class RangListFragment : Fragment() {

    private var _binding: FragmentRangListBinding?=null
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var userAdapter: UserAdapter
    private val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        _binding= FragmentRangListBinding.inflate(inflater, container, false)
        userViewModel.getUsers(){
            userAdapter= UserAdapter(requireContext(), userViewModel.users!!)
            binding.rangList.adapter=userAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(userViewModel.user!!.imageURL!=null)
        {
            var profileImage=requireView().findViewById<ImageView>(R.id.profile_image_view)
            Glide.with(requireContext())
                .load(userViewModel.user!!.imageURL)
                .into(profileImage)
        }

        binding.tvCurrentName.text= userViewModel.user!!.name
        binding.tvCurrentLastName.text=userViewModel.user!!.lastName
        binding.tvCurrentUserame.text=userViewModel.user!!.userName
        binding.tvCurrentEmailValue.text=userViewModel.user!!.email
        binding.tvCurrentPhoneValue.text=userViewModel.user!!.phoneNumber
        binding.tvCurrentPointsValue.text=userViewModel.user!!.points.toString()
        binding.rangListMyDevices.setOnClickListener {
            findNavController().navigate(R.id.action_rangListFragment_to_myDevicesListFragment)
        }

    }


}