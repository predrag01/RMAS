package elfak.mosis.underradar.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import elfak.mosis.underradar.databinding.FragmentDeviceDetailsBinding
import elfak.mosis.underradar.viewmodels.CommentViewModel
import elfak.mosis.underradar.viewmodels.DeviceViewModel
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

        binding.username.text= userViewModel.user!!.userName
        binding.deviceTitle.text=deviceViewModel.device!!.title
        binding.deviceDescription.text=deviceViewModel.device!!.description
        binding.deviceLikes.text=deviceViewModel.device!!.like.toString()
        binding.deviceDislikes.text=deviceViewModel.device!!.dislike.toString()

        binding.deviceButtonLike.setOnClickListener {
            deviceViewModel.like(userViewModel.user!!)
            binding.deviceLikes.text=deviceViewModel.device!!.like.toString()
        }

        binding.deviceButtonDislike.setOnClickListener {
            deviceViewModel.dislike(userViewModel.user!!)
            binding.deviceDislikes.text=deviceViewModel.device!!.dislike.toString()
        }

        binding.deviceButtonAddComment.setOnClickListener {
            if(binding.deviceDetailsEditTextComment.text.isNotBlank())
            {
                commentViewModel.addComment(binding.deviceDetailsEditTextComment.text.toString(),
                    deviceViewModel.device!!.id, userViewModel.user!!)
                (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)
                binding.deviceDetailsEditTextComment.text.clear()
            }
        }

        binding.deviceButtonComment.setOnClickListener {
            bottomSheetDialogFragment!!.show(childFragmentManager, "BottomSheetDialog")
        }
    }

}