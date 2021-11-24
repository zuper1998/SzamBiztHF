package com.e.caffadminapp.Controller.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.caffadminapp.Adapter.UsersAdapter
import com.e.caffadminapp.Network.Response.GetAllUserResponse
import com.e.caffadminapp.Network.Service.UserService
import com.e.caffadminapp.R
import com.e.caffadminapp.databinding.FragmentUserManagementBinding
import com.e.caffadminapp.model.AdminData
import com.e.szambizthfapplibrary.model.User
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.collections.ArrayList

class UserManagementFragment : Fragment() {

    private lateinit var binding: FragmentUserManagementBinding
    private lateinit var retrofit: Retrofit
    private var users: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_management,container,false) as FragmentUserManagementBinding
        val adapter = UsersAdapter(users)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.layoutManager = LinearLayoutManager(binding.root.context)
        getAll(adapter)

        binding.swipetorefresh.setOnRefreshListener {
            getAll(adapter)
            binding.swipetorefresh.isRefreshing = false
        }

        binding.newUserButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.NewUserFragment)
        }

        return binding.root
    }

    private fun getAll(adapter: UsersAdapter) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(UserService::class.java).getAll("Bearer " + AdminData.getToken())
        call.enqueue(object : Callback<List<GetAllUserResponse>> {
            override fun onResponse(call: Call<List<GetAllUserResponse>>, message: Response<List<GetAllUserResponse>>) {
                if (message.code() == 200) {
                    adapter.updateData(message.body()!! as ArrayList<GetAllUserResponse>)
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<List<GetAllUserResponse>>, t: Throwable) {
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}