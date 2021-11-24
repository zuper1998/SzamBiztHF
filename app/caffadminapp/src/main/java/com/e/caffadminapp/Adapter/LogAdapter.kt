package com.e.caffadminapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.caffadminapp.model.Log
import com.e.caffadminapp.databinding.LogRowBinding

class LogAdapter(private var logs:ArrayList<Log>): RecyclerView.Adapter<LogAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: LogRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(log: Log) {
            binding.log = log
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = LogRowBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(logs[position])
    }


    override fun getItemCount() = logs.size

    fun updateData(data: ArrayList<Log>) {
        logs = data
    }

}