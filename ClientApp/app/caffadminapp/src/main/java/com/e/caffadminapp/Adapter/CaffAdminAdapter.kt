package com.e.caffadminapp.Adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.e.caffadminapp.R
import com.e.caffadminapp.model.AdminData
import com.e.szambizthfapplibrary.databinding.CaffRowBinding
import com.e.szambizthfapplibrary.network.Response.GetAllCaffResponse
import java.util.*
import kotlin.collections.ArrayList

class CaffAdminAdapter(private var caffs:ArrayList<GetAllCaffResponse>): RecyclerView.Adapter<CaffAdminAdapter.ViewHolder>() {


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
                        colorInt[j] = Color.rgb(
                            caff.caff!!.ciffs?.get(0)?.rgb_values!![i],
                            caff.caff!!.ciffs?.get(0)?.rgb_values!![i + 1],
                            caff.caff!!.ciffs?.get(0)?.rgb_values!![i + 2]
                        )
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
            AdminData.setSelectedCaff(caffs[position])
            Navigation.findNavController(viewHolder.itemView).navigate(R.id.CaffAdminDetailsFragment)
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