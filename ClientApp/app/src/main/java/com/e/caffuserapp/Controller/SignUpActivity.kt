package com.e.caffuserapp.Controller

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.e.caffuserapp.Netwrok.Service.UserService
import com.e.caffuserapp.R
import com.e.caffuserapp.databinding.ActivitySignupBinding
import com.e.szambizthfapplibrary.network.Request.SignUpRequest
import com.e.szambizthfapplibrary.network.Response.LoginResponse
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SignUpActivity  : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup) as ActivitySignupBinding

        binding.signUpButton.setOnClickListener {
            val signUpRequest = SignUpRequest(binding.editTextUsernameEdit.text.toString(),binding.editTextPasswordEdit.text.toString(),binding.editTextEmailEdit.text.toString())
            if(!checkInput(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getEmail())) return@setOnClickListener
            signUp(signUpRequest)
        }
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

    private fun signUp(signUpRequest: SignUpRequest) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(UserService::class.java).signUp(signUpRequest)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, message: Response<Void>) {
                if (message.code() == 200) {
                    val intent = Intent(binding.root.context, LoginActivity::class.java)
                    binding.root.context.startActivity(intent)
                }
                if(message.code() == 401)  Toast.makeText(binding.root.context, "username or email is already in use", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}