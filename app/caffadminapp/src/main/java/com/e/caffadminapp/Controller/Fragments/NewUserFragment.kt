package com.e.caffadminapp.Controller.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.e.caffadminapp.Network.Response.GetAllUserResponse
import com.e.caffadminapp.Network.Service.UserService
import com.e.caffadminapp.R
import com.e.caffadminapp.databinding.FragmentNewUserBinding
import com.e.caffadminapp.databinding.FragmentUserManagementBinding
import com.e.caffadminapp.model.AdminData
import com.e.caffadminapp.model.RoleEnum
import com.e.szambizthfapplibrary.network.Request.SignUpRequest
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class NewUserFragment : Fragment() {
    private lateinit var binding: FragmentNewUserBinding
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_user,container,false) as FragmentNewUserBinding
        binding.roleSpinner.adapter = ArrayAdapter<RoleEnum>(binding.root.context,android.R.layout.simple_spinner_dropdown_item, RoleEnum.values())

        binding.saveButton.setOnClickListener {
            var signUpRequest = SignUpRequest(binding.editTextUsernameEdit.text.toString(),binding.editTextPasswordEdit.text.toString(),binding.editTextEmailEdit.text.toString(),binding.roleSpinner.selectedItem.toString())
            if(!checkInput(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getEmail())) return@setOnClickListener
            addUser(signUpRequest)
        }

        return binding.root
    }

    private fun addUser(signUpRequest: SignUpRequest) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(UserService::class.java).addUser(signUpRequest, "Bearer " + AdminData.getToken())
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, message: Response<Void>) {
                if (message.code() == 200) {
                    Navigation.findNavController(binding.root).navigate(R.id.UserManagementFragment)
                    Toast.makeText(binding.root.context, "Successful addition", Toast.LENGTH_SHORT).show()
                }
                if(message.code() == 401) Toast.makeText(binding.root.context, "username or email already in use", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkInput(pw: String, username:String,email: String): Boolean{
        var result: Boolean = true
        if(pw.equals(""))
        {
            binding.editTextPasswordEdit.setError("Password must not be empty")
            result = false
        }
        if(email.equals(""))
        {
            binding.editTextEmailEdit.setError("Email must not be empty")
            result = false
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.editTextEmailEdit.setError("Not Valid email")
            result = false
        }
        if(username.equals(""))
        {
            binding.editTextUsernameEdit.setError("Username must not be empty")
            result = false
        }
        return result
    }

}