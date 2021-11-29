package com.e.caffadminapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.e.caffadminapp.Network.Service.CommentService
import com.e.caffadminapp.model.AdminData
import com.e.szambizthfapplibrary.databinding.CommentRowBinding
import com.e.szambizthfapplibrary.network.Response.GetCommentResponse
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CommentAdminAdapter(private var comments:ArrayList<GetCommentResponse>): RecyclerView.Adapter<CommentAdminAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CommentRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: GetCommentResponse) {
            binding.tvComment.text = comment.text
            binding.tvCommentUser.text = comment.username
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommentRowBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(comments[position])
        viewHolder.itemView.setOnLongClickListener {
            deleteComment(comments[position].id!!)
            comments.removeAt(position)
            notifyDataSetChanged()
            true
        }
    }


    override fun getItemCount() = comments.size

    fun updateData(data: ArrayList<GetCommentResponse>) {
        comments = ArrayList<GetCommentResponse>()
        for(comment in data) {
            comments.add(comment)
        }
    }

    fun removeAt(pos: Int) {
        comments.removeAt(pos)
    }

    fun getID(pos: Int): UUID {
        return comments[pos].id!!
    }

    private fun deleteComment(id: UUID){
        val retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(CommentService::class.java).deleteComment("Bearer " + AdminData.getToken(), id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, message: Response<Void>) {
                if (message.code() == 200) {
                    println("yup")

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("nop")
            }
        })
    }

}