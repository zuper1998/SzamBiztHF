package com.e.caffuserapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.szambizthfapplibrary.databinding.CommentRowBinding
import com.e.szambizthfapplibrary.network.Response.GetCommentResponse
import java.util.*
import kotlin.collections.ArrayList

class CommentAdapter(private var comments:ArrayList<GetCommentResponse>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
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

}