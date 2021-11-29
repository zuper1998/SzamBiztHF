package com.e.caffuserapp.Controller.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.caffuserapp.Adapter.CaffAdapter
import com.e.caffuserapp.Netwrok.Response.GetAllCaffResponse
import com.e.caffuserapp.Netwrok.Service.CaffService
import com.e.caffuserapp.R
import com.e.caffuserapp.databinding.FragmentHomeBinding
import com.e.caffuserapp.model.UserData
import com.e.szambizthfapplibrary.model.User
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: CaffAdapter
    private var caffs: ArrayList<GetAllCaffResponse> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        adapter = CaffAdapter(caffs)
        binding.rvHomePage.adapter = adapter
        getAll(adapter)

        binding.btnSearch.setOnClickListener {
            println("keres")
            if(binding.svHomePage.text.toString().isEmpty()){
                println("keresures")
                adapter.updateData(caffs)
            }
            else{
                adapter.updateWithFilter(binding.svHomePage.text.toString(), caffs)
            }
            adapter.notifyDataSetChanged()
        }

        /*binding.swipetorefreshHome.setOnRefreshListener {
            getAll(adapter)
            binding.swipetorefreshHome.isRefreshing = false
        }*/

        /*binding.newUserButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.NewUserFragment)
        }*/

        return binding.root
    }

    private fun getAll(adapter: CaffAdapter) {
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(CaffService::class.java).getAll("Bearer " + UserData.getToken())
        call.enqueue(object : Callback<List<GetAllCaffResponse>> {
            override fun onResponse(call: Call<List<GetAllCaffResponse>>, message: Response<List<GetAllCaffResponse>>) {
                if (message.code() == 200) {
                    println("yup")
                    caffs = message.body()!! as ArrayList<GetAllCaffResponse>
                    adapter.updateData(message.body()!! as ArrayList<GetAllCaffResponse>)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<GetAllCaffResponse>>, t: Throwable) {
                println("nop")
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}