package elfak.mosis.underradar.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import elfak.mosis.underradar.R
import elfak.mosis.underradar.data.User
import elfak.mosis.underradar.databinding.FragmentRegisterBinding
import elfak.mosis.underradar.viewmodels.UserViewModel
import android.content.Intent as Intent


class RegisterFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : AlertDialog
    private var _binding: FragmentRegisterBinding?=null
    private  lateinit var database: DatabaseReference
    private val userViewModel: UserViewModel by activityViewModels()
    private var imageURI: Uri? = null
    private lateinit var storageRef : StorageReference

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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        database= Firebase.database.reference
        storageRef=FirebaseStorage.getInstance().reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButtonRegister.setOnClickListener{
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view.windowToken, 0)
            if(checkInputs()){

                var user: User = User(
                    name=binding.registerEditTextName.text.toString(),
                    lastName=binding.registerEditTextLastname.text.toString(),
                    userName = binding.registerEditTextUsername.text.toString(),
                    phoneNumber = binding.registerEditTextPhoneNumber.text.toString(),
                    email = binding.registerEditTextEmail.text.toString(),
                )

                createAccount(user, binding.registerEditTextPassword.text.toString())
            }
        }

        binding.registerButtonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerImageViewProfilePhoto.setOnClickListener{
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 2)
            } else {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode==2 && resultCode==RESULT_OK && data!=null)
        {
            imageURI=data.data
            binding.registerImageViewProfilePhoto.setImageURI(imageURI)
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

        var confirmPassword=binding.registerEditTextConfirmpassword.text
        if(confirmPassword.isBlank())
        {
            binding.textInputConfirmpassword.error="Required"
            check= false
        }

        if(password.toString()!=confirmPassword.toString())
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

    private fun createAccount(user: User, password: String)
    {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { it ->
                user.id=it.user!!.uid
                if(imageURI!==null)
                {
                    storageRef.child("test.jpg").putFile(imageURI!!).addOnSuccessListener {

                        //Toast.makeText(context, "Postavljeno", Toast.LENGTH_SHORT).show()

                        storageRef.child("test.jpg").downloadUrl.addOnSuccessListener {uri->
                            user.imageURL=uri.toString()
                            database.child("Users").child(user.id).setValue(user).addOnSuccessListener {
                                userViewModel.user=user
                                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                            }.addOnFailureListener {
                                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                            }

                            }.addOnFailureListener{
                            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                        }.addOnFailureListener{
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    database.child("Users").child(user.id).setValue(user).addOnSuccessListener {
                        userViewModel.user=user
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }.addOnFailureListener {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener{
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        //database.child("Users").child(user.id).setValue(user)
    }
}