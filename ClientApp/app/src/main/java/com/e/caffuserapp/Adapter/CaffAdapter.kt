package com.e.caffuserapp.Adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.e.szambizthfapplibrary.network.Response.GetAllCaffResponse
import com.e.caffuserapp.R
import com.e.caffuserapp.model.UserData
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Color.rgb

import com.e.szambizthfapplibrary.databinding.CaffRowBinding


class CaffAdapter(private var caffs:ArrayList<GetAllCaffResponse>): RecyclerView.Adapter<CaffAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: CaffRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(caff: GetAllCaffResponse) {
            println("cc")
            binding.tvCaffTitle.text = caff.title

            if(caff.caff != null){
                val h = caff.caff!!.ciffs?.get(1)?.height
                val w = caff.caff!!.ciffs?.get(1)?.width
                if(w != null && h != null){
                    var colorInt: IntArray = IntArray(w * h)
                    var j = 0
                    for(i in 0 until caff.caff!!.ciffs?.get(0)?.rgb_values?.size!! step 3){
                        //caff.caff.ciffs?.get(0)?.rgb_values!![i]/255
                        colorInt[j] = rgb(caff.caff!!.ciffs?.get(0)?.rgb_values!![i], caff.caff!!.ciffs?.get(0)?.rgb_values!![i + 1], caff.caff!!.ciffs?.get(0)?.rgb_values!![i + 2])
                        j++
                    }
                    val bitmap = Bitmap.createBitmap(colorInt, w, h, Bitmap.Config.ARGB_8888)
                    binding.ivCaff.setImageBitmap(bitmap)
                }

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CaffRowBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(caffs[position])
        viewHolder.itemView.setOnClickListener {
            UserData.setSelectedCaff(caffs[position])
            Navigation.findNavController(viewHolder.itemView).navigate(R.id.CaffDetailsFragment)
            true
        }
    }

    override fun getItemCount() = caffs.size

    fun updateData(data: ArrayList<GetAllCaffResponse>) {
        caffs = ArrayList<GetAllCaffResponse>()
        for(caff in data) {
            caffs.add(caff)
        }

    }

    fun updateWithFilter(filter: String, ccs: ArrayList<GetAllCaffResponse>){
        val tmpL = mutableListOf<GetAllCaffResponse>()
        for(c in ccs){
            if(c.title!!.contains(filter, ignoreCase = true)){
                tmpL.add(c)
            }
        }
        updateData(ArrayList(tmpL))
    }

    fun removeAt(pos: Int) {
        caffs.removeAt(pos)
    }

    fun getID(pos: Int): UUID {
        return caffs[pos].id!!
    }


}