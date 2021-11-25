package com.e.caffadminapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.e.caffadminapp.Network.Response.GetAllUserResponse
import com.e.caffadminapp.R
import com.e.caffadminapp.databinding.UserRowBinding
import com.e.caffadminapp.model.AdminData
import com.e.szambizthfapplibrary.model.User
import java.util.*
import kotlin.collections.ArrayList

class UsersAdapter(private var users:ArrayList<User>): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: UserRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserRowBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(users[position])
        viewHolder.itemView.setOnLongClickListener {
            AdminData.setEditedUser(users[position])
            Navigation.findNavController(viewHolder.itemView).navigate(R.id.AccountDetailsFragment)
            true
        }
    }


    override fun getItemCount() = users.size

    fun updateData(data: ArrayList<GetAllUserResponse>) {
        users = ArrayList<User>()
        for(user in data) {
            users.add(User(user.getId(), user.getUsername(), user.getEmail()))
        }
    }

    fun removeAt(pos: Int) {
        users.removeAt(pos)
    }

    fun getID(pos: Int): UUID {
        return users[pos].getId()
    }

}