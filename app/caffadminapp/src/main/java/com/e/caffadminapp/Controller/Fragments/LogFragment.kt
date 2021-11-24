package com.e.caffadminapp.Controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.caffadminapp.Adapter.LogAdapter
import com.e.caffadminapp.Controller.MainMenuActivity
import com.e.caffadminapp.Network.Service.LogService
import com.e.caffadminapp.Network.Service.UserService
import com.e.caffadminapp.R
import com.e.caffadminapp.databinding.FragmentLogBinding
import com.e.caffadminapp.model.AdminData
import com.e.caffadminapp.model.Log
import com.e.szambizthfapplibrary.network.Response.LoginResponse
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LogFragment : Fragment() {

    private lateinit var binding: FragmentLogBinding
    private lateinit var retrofit: Retrofit
    private var logs: ArrayList<Log> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log,container,false) as FragmentLogBinding
        val adapter = LogAdapter(logs)
        binding.rvLogs.adapter = adapter
        binding.rvLogs.layoutManager = LinearLayoutManager(binding.root.context)
        getLogs(adapter)

        binding.swipetorefresh.setOnRefreshListener {
            getLogs(adapter)
            binding.swipetorefresh.isRefreshing = false
        }

        return binding.root
    }

    private fun getLogs(adapter: LogAdapter) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(LogService::class.java).getAll("Bearer " + AdminData.getToken())
        call.enqueue(object : Callback<List<Log>> {
            override fun onResponse(call: Call<List<Log>>, message: Response<List<Log>>) {
                if (message.code() == 200) {
                    adapter.updateData(message.body()!! as ArrayList<Log>)
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<List<Log>>, t: Throwable) {
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}