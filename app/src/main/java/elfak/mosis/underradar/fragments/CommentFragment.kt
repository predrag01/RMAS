package elfak.mosis.underradar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import elfak.mosis.underradar.R
import elfak.mosis.underradar.adapters.CommentAdapter
import elfak.mosis.underradar.databinding.FragmentCommentBinding
import elfak.mosis.underradar.viewmodels.CommentViewModel

class CommentFragment : BottomSheetDialogFragment() {

    private lateinit var commentAdapter: CommentAdapter
    private val commentViewModel: CommentViewModel by activityViewModels()
    private var _binding: FragmentCommentBinding? =null
    private val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        commentAdapter= CommentAdapter(requireContext(), commentViewModel.comments!!)
        _binding=FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.commentList.adapter=commentAdapter
    }
}