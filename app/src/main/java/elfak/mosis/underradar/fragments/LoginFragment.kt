package elfak.mosis.underradar.fragments

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.User
import elfak.mosis.underradar.databinding.FragmentLoginBinding
import elfak.mosis.underradar.viewmodels.LoggedUserViewModel
import elfak.mosis.underradar.viewmodels.UserViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding?=null
    private lateinit var progressDialog: ProgressDialog
    private val loggedUserViewModel: LoggedUserViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        progressDialog= ProgressDialog(requireContext())
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButtonLogin.apply {
            setOnClickListener {
                (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)

                if(checkInputs())
                {
                    progressDialog.show()
                    var email=binding.loginEditTextEmail.text.toString()
                    var password=binding.loginEditTextPassword.text.toString()

                    loggedUserViewModel.login(email, password,
                    onSuccess = {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        progressDialog.dismiss()
                        Toast.makeText(context, "Logged as "+ loggedUserViewModel.user!!.userName, Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        progressDialog.dismiss()
                    })
                }
            }
        }

        binding.loginButtonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun checkInputs(): Boolean
    {
        var check:Boolean=true

        if(binding.loginEditTextEmail.text.toString().isBlank())
        {
            check=false
            binding.textInputEmail.error="Field cannot be empty"
        }
        if(binding.loginEditTextPassword.text.toString().isBlank())
        {
            check=false
            binding.textInputPassword.error="Field cannot be empty"
        }

        return check
    }
}