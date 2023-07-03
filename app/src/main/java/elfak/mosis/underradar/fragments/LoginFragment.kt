package elfak.mosis.underradar.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.underradar.R
import elfak.mosis.underradar.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private var _binding: FragmentLoginBinding?=null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        firebaseAuth=FirebaseAuth.getInstance()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
                    var email=binding.loginEditTextEmail.text.toString()
                    var password=binding.loginEditTextPassword.text.toString()

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful)
                        {
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                        else
                        {
                            Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
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