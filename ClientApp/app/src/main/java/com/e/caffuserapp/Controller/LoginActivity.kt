package com.e.caffuserapp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.e.caffuserapp.Netwrok.Service.UserService
import com.e.caffuserapp.R
import com.e.caffuserapp.databinding.ActivityLoginBinding
import com.e.caffuserapp.model.UserData


import com.e.szambizthfapplibrary.network.Request.LoginRequest
import com.e.szambizthfapplibrary.network.Response.LoginResponse
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login) as ActivityLoginBinding


        binding.loginbutton.setOnClickListener {
            val loginRequest = LoginRequest(binding.editTextPersonName.text.toString(),binding.editTextPassword.text.toString())
            if(!checkInput(loginRequest.getUsername(), loginRequest.getPassword())) return@setOnClickListener
            login(loginRequest)
        }

        binding.textViewSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkInput(pw: String, username:String): Boolean{
        if(pw.equals("") && username.equals(""))
        {
            binding.editTextPersonName.setError("Username must not be empty")
            binding.editTextPassword.setError("Password must not be empty")
            return false
        }
        else if(pw.equals(""))
        {
            binding.editTextPassword.setError("Password must not be empty")
            return false
        }
        else if(username.equals(""))
        {
            binding.editTextPersonName.setError("Username must not be empty")
            return false
        }
        else return true
    }

    private fun login(loginRequest: LoginRequest) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(UserService::class.java).login(loginRequest = loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, message: Response<LoginResponse>) {
                if (message.code() == 200) {
                    UserData.login(message.body()!!)
                    val intent = Intent(binding.root.context, MainMenuActivity::class.java)
                    binding.root.context.startActivity(intent)
                }
                if(message.code() == 401)  Toast.makeText(binding.root.context, "username or password is wrong", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}