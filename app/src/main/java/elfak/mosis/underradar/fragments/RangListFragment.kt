package elfak.mosis.underradar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import elfak.mosis.underradar.R
import elfak.mosis.underradar.adapters.UserAdapter
import elfak.mosis.underradar.databinding.FragmentRangListBinding
import elfak.mosis.underradar.viewmodels.LoggedUserViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel
import elfak.mosis.underradar.viewmodels.UsersViewModel

class RangListFragment : Fragment() {

    private var _binding: FragmentRangListBinding?=null
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val loggedUserViewModel: LoggedUserViewModel by activityViewModels()
    private lateinit var userAdapter: UserAdapter
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        _binding= FragmentRangListBinding.inflate(inflater, container, false)
        usersViewModel.getUsers(onDataLoaded = {
            userAdapter= UserAdapter(requireContext(), usersViewModel.users!!)
            binding.rangList.adapter=userAdapter
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(loggedUserViewModel.user!!.imageURL!=null)
        {
            var profileImage=requireView().findViewById<ImageView>(R.id.profile_image_view)
            Glide.with(requireContext())
                .load(loggedUserViewModel.user!!.imageURL)
                .into(profileImage)
        }

        binding.tvCurrentName.text= loggedUserViewModel.user!!.name
        binding.tvCurrentLastName.text=loggedUserViewModel.user!!.lastName
        binding.tvCurrentUserame.text=loggedUserViewModel.user!!.userName
        binding.tvCurrentEmailValue.text=loggedUserViewModel.user!!.email
        binding.tvCurrentPhoneValue.text=loggedUserViewModel.user!!.phoneNumber
        binding.tvCurrentPointsValue.text=loggedUserViewModel.user!!.points.toString()
        binding.rangListMyDevices.setOnClickListener {
            findNavController().navigate(R.id.action_rangListFragment_to_myDevicesListFragment)
        }

    }

    override fun onPause() {
        super.onPause()
        usersViewModel.users=null
    }

    override fun onDestroy() {
        super.onDestroy()
        usersViewModel.users=null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        usersViewModel.users=null
    }
}