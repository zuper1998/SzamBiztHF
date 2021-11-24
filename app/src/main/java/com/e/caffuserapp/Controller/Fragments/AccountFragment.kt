package com.e.caffuserapp.Controller.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.e.caffuserapp.Netwrok.Service.UserService
import com.e.caffuserapp.R
import com.e.caffuserapp.model.UserData
import com.e.szambizthfapplibrary.databinding.FragmentAccountBinding
import com.e.szambizthfapplibrary.model.User
import com.e.szambizthfapplibrary.network.Request.UpdateUserRequest
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.e.szambizthfapplibrary.R.layout.fragment_account,container,false) as FragmentAccountBinding
        binding.user = User("","", UserData.getEmail())
        binding.saveButton.setOnClickListener {
            var updateUserRequest = UpdateUserRequest(binding.editTextEmailEdit.text.toString(), binding.editTextPasswordEdit.text.toString())
            if(!checkEmailInput(updateUserRequest.getEmail()) && !checkPWInput(updateUserRequest.getPassword())) return@setOnClickListener
            update(updateUserRequest)
        }
        return binding.root
    }

    private fun update(updateUserRequest: UpdateUserRequest) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(UserService::class.java).updateAccount(updateUserRequest = updateUserRequest, "Bearer " + UserData.getToken(), UserData.getId())
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, message: Response<Void>) {
                if (message.code() == 200) {
                    Toast.makeText(binding.root.context, "Successful update", Toast.LENGTH_SHORT).show()
                    UserData.setEmail(updateUserRequest.getEmail())
                }
                if (message.code() == 401)  Toast.makeText(binding.root.context, "no such user id", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkEmailInput(email: String): Boolean{
        var result = true
        if(email == "")
        {
            binding.editTextEmailEdit.error = "Email must not be empty"
            result = false
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.editTextEmailEdit.error = "Not Valid email"
            result = false
        }
        return result
    }

    private fun checkPWInput(password:String): Boolean {
        var result = true
        if(password == "")
        {
            binding.editTextPasswordEdit.error = "Password must not be empty"
            result = false
        }
        return result
    }

}