package com.e.caffadminapp.Adapter

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.e.caffadminapp.Network.Request.UpdateCommentRequest
import com.e.caffadminapp.Network.Service.CommentService
import com.e.caffadminapp.databinding.CommentAdminRowBinding
import com.e.caffadminapp.model.AdminData
import com.e.szambizthfapplibrary.databinding.CommentRowBinding
import com.e.szambizthfapplibrary.model.Comment
import com.e.szambizthfapplibrary.network.Response.GetCommentResponse
import com.e.szambizthfapplibrary.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CommentAdminAdapter(private var comments:ArrayList<GetCommentResponse>, val context: Context?): RecyclerView.Adapter<CommentAdminAdapter.ViewHolder>() {



    inner class ViewHolder(val binding: CommentAdminRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: GetCommentResponse) {
            binding.etCommentAdminText.setText(comment.text)
            binding.tvCommentUser.text = comment.username
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommentAdminRowBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(comments[position])
        viewHolder.itemView.setOnLongClickListener {
            editDialog(position, viewHolder.binding.etCommentAdminText.text.toString())
            true
        }
    }

    private fun editDialog(pos: Int, text: String){
        val dialog = AlertDialog.Builder(context!!)
            .setTitle("Comment Edit")
            .setMessage("Do you want to update or delete the comment?")
            .setPositiveButton("Edit", DialogInterface.OnClickListener { _, _ ->
                updateComment(comments[pos].id!!, text)
                //comments[pos].text = text
                //notifyItemChanged(pos)
            })
            .setNegativeButton("Delete", DialogInterface.OnClickListener { _, _ ->
                deleteComment(comments[pos].id!!)
                comments.removeAt(pos)
                notifyDataSetChanged()
            })
        val dail = dialog.create()
        dail.show()
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
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

    private fun updateComment(id: UUID, text: String){
        val retrofit = RetrofitClient.getInstance()
        val request = UpdateCommentRequest()
        request.text = text
        val call = retrofit.create(CommentService::class.java).updateComment( request,"Bearer " + AdminData.getToken(), id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, message: Response<Void>) {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

}