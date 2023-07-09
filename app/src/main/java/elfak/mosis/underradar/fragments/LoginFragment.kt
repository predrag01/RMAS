package elfak.mosis.underradar.fragments

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
import elfak.mosis.underradar.viewmodels.UserViewModel

class LoginFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private var _binding: FragmentLoginBinding?=null
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var database: DatabaseReference

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
        database=Firebase.database.reference
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

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                        database.child("Users").child(it.user!!.uid).addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                userViewModel.user=snapshot.getValue(User::class.java)
                                Toast.makeText(context, userViewModel.user!!.name, Toast.LENGTH_SHORT).show()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        })
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
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