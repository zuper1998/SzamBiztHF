package com.e.caffadminapp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.e.caffadminapp.Network.Service.UserService
import com.e.caffadminapp.R
import com.e.caffadminapp.databinding.ActivityLoginBinding
import com.e.caffadminapp.model.AdminData
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
                val loginRequest = LoginRequest()
                loginRequest.setUsername(binding.editTextPersonName.text.toString())
                loginRequest.setPassword(binding.editTextPassword.text.toString())
                if(!checkInput(loginRequest.getUsername(), loginRequest.getPassword())) return@setOnClickListener
                login(loginRequest)
            }

        }

        private fun checkInput(pw: String, username:String): Boolean{
            if(pw.equals("") && username.equals(""))
            {
                binding.editTextPersonName.error = "Username must not be empty"
                binding.editTextPassword.error = "Password must not be empty"
                return false
            }
            else if(pw.equals(""))
            {
                binding.editTextPassword.error = "Password must not be empty"
                return false
            }
            else if(username.equals(""))
            {
                binding.editTextPersonName.error = "Username must not be empty"
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
                        AdminData.login(message.body()!!)
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