package elfak.mosis.underradar.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.fido.fido2.api.common.RequestOptions
import elfak.mosis.underradar.R
import elfak.mosis.underradar.databinding.FragmentDeviceDetailsBinding
import elfak.mosis.underradar.viewmodels.CommentViewModel
import elfak.mosis.underradar.viewmodels.DeviceViewModel
import elfak.mosis.underradar.viewmodels.LoggedUserViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel


class DeviceDetailsFragment : Fragment() {

    private var _binding: FragmentDeviceDetailsBinding?=null
    private val userViewModel: UserViewModel by activityViewModels()
    private val deviceViewModel: DeviceViewModel by activityViewModels()
    private val commentViewModel: CommentViewModel by activityViewModels()
    private var bottomSheetDialogFragment: CommentFragment? =null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentDeviceDetailsBinding.inflate(inflater, container, false)
        commentViewModel.getCommentsForDevice(deviceViewModel.device!!.id)
        bottomSheetDialogFragment=CommentFragment()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(deviceViewModel.device!!.imageURL!=null)
        {
            var deviceImage=requireView().findViewById<ImageView>(R.id.device_details_image_view)
            Glide.with(requireContext())
                .load(deviceViewModel.device!!.imageURL)
                .into(deviceImage)
        }

        if(userViewModel.owner!!.imageURL!=null)
        {
            var profileImage=requireView().findViewById<ImageView>(R.id.device_details_profile_image_view)
            Glide.with(requireContext())
                .load(userViewModel.owner!!.imageURL)
                .into(profileImage)
        }

        binding.username.text= userViewModel.owner!!.userName
        binding.deviceTitle.text=deviceViewModel.device!!.title
        binding.deviceDescription.text=deviceViewModel.device!!.description
        binding.deviceType.text=deviceViewModel.device!!.type
        binding.deviceLikes.text=deviceViewModel.device!!.like.toString()
        binding.deviceDislikes.text=deviceViewModel.device!!.dislike.toString()

        binding.deviceButtonLike.setOnClickListener {
            deviceViewModel.like(userViewModel.owner!!)
            binding.deviceLikes.text=deviceViewModel.device!!.like.toString()
        }

        binding.deviceButtonDislike.setOnClickListener {
            deviceViewModel.dislike(userViewModel.owner!!)
            binding.deviceDislikes.text=deviceViewModel.device!!.dislike.toString()
        }

        binding.deviceButtonComment.setOnClickListener {
            bottomSheetDialogFragment!!.show(childFragmentManager, "BottomSheetDialog")
        }
    }

    override fun onPause() {
        super.onPause()
        userViewModel.owner=null
    }

    override fun onDestroy() {
        super.onDestroy()
        userViewModel.owner=null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.owner=null
    }
}