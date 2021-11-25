package com.e.caffadminapp.Controller.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.e.caffadminapp.Adapter.UsersAdapter
import com.e.caffadminapp.Network.Service.UserService
import com.e.caffadminapp.R
import com.e.caffadminapp.databinding.FragmentAccountDetailsBinding
import com.e.caffadminapp.databinding.FragmentUserManagementBinding
import com.e.caffadminapp.model.AdminData
import com.e.szambizthfapplibrary.network.Request.UpdateUserRequest
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class AccountDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAccountDetailsBinding
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_details,container,false) as FragmentAccountDetailsBinding
        binding.user = AdminData.getEditedUser()

        binding.deleteButton.setOnClickListener {
            delete(AdminData.getEditedUser()!!.getId())
        }

        binding.saveButton.setOnClickListener {
            var updateUserRequest = UpdateUserRequest(binding.editTextEmailEdit.text.toString(), binding.editTextPasswordEdit.text.toString())
            if(!checkEmailInput(updateUserRequest.getEmail())) return@setOnClickListener
            update(AdminData.getEditedUser()!!.getId(), updateUserRequest)
        }

        return binding.root
    }

    private fun delete(id: UUID) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(UserService::class.java).deleteUser("Bearer " + AdminData.getToken(), id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, message: Response<Void>) {
                if (message.code() == 200) {
                    Toast.makeText(binding.root.context, "Successful deletion", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).navigate(R.id.UserManagementFragment)
                    AdminData.setEditedUser(null)
                }
                if (message.code() == 401)  Toast.makeText(binding.root.context, "no such user id", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun update(id: UUID, updateUserRequest: UpdateUserRequest) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(UserService::class.java).updateUser(updateUserRequest, "Bearer " + AdminData.getToken(), id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, message: Response<Void>) {
                if (message.code() == 200) {
                    Toast.makeText(binding.root.context, "Successful update", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root).navigate(R.id.UserManagementFragment)
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


}