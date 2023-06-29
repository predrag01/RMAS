package elfak.mosis.underradar.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.User
import elfak.mosis.underradar.databinding.FragmentLoginBinding
import elfak.mosis.underradar.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : AlertDialog
    private var _binding: FragmentRegisterBinding?=null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        firebaseAuth=FirebaseAuth.getInstance()
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButtonRegister.apply {
            setOnClickListener{
                (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)
                if(checkInputs()){

                    var user: User = User(
                        name=binding.registerEditTextName.text.toString(),
                        lastName=binding.registerEditTextLastname.text.toString(),
                        userName = binding.registerEditTextUsername.text.toString(),
                        phoneNumber = binding.registerEditTextPhoneNumber.text.toString(),
                        email = binding.registerEditTextEmail.text.toString()
                    )

                    createAccount(user, binding.registerEditTextPassword.text.toString(), savedInstanceState)
                }
            }
        }

        binding.registerButtonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun checkInputs(): Boolean
    {
        var check:Boolean=true

        var name = binding.registerEditTextName.text
        if(name.isBlank())
        {
            binding.textInputName.error="Required"
            check= false
        }

        var lastname=binding.registerEditTextLastname.text
        if(lastname.isBlank())
        {
            binding.textInputLastName.error="Required"
            check= false
        }

        var username=binding.registerEditTextUsername.text
        if(username.isBlank())
        {
            binding.textInputUsername.error="Required"
            check= false
        }

        var email=binding.registerEditTextEmail.text
        if(email.isBlank())
        {
            binding.textInputEmail.error="Required"
            check= false
        }

        var password=binding.registerEditTextPassword.text
        if(password.isBlank())
        {
            binding.textInputPassword.error="Required"
            check= false
        }

        var ConfirmPassword=binding.registerEditTextConfirmpassword.text
        if(ConfirmPassword.isBlank())
        {
            binding.textInputConfirmpassword.error="Required"
            check= false
        }

        if(password!=ConfirmPassword)
        {
            binding.textInputConfirmpassword.error="Confirm password must match with password"
        }

        var phone=binding.registerEditTextPhoneNumber.text
        if(phone.isBlank())
        {
            binding.textInputPhoneNumber.error="Required"
            check= false
        }

        if(check)
        {
            return true
        }
        else
        {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Invalid form")
                .setMessage("Please check your input and try again")
                .setPositiveButton("Ok")
                {d, _ ->d.dismiss()}
                .show()
            return false
        }
    }

    private fun createAccount(user: User, password: String, savedInstanceState: Bundle?)
    {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener{
            if(it.isSuccessful){
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }
            else
            {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}