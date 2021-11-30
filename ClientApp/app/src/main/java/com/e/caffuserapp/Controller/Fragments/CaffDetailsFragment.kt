package com.e.caffuserapp.Controller.Fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.e.caffuserapp.Adapter.CommentAdapter
import com.e.caffuserapp.Netwrok.Service.CaffService
import com.e.szambizthfapplibrary.network.Response.GetAllCaffResponse
import com.e.caffuserapp.Netwrok.Service.CommentService
import com.e.caffuserapp.databinding.FragmentCaffDetailsBinding
import com.e.caffuserapp.model.UserData
import com.e.szambizthfapplibrary.network.Response.GetCommentResponse
import com.e.szambizthfapplibrary.network.RetrofitClient
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.io.InputStream


class CaffDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCaffDetailsBinding
    private var bitmaps = mutableListOf<Bitmap>()
    private var durs = mutableListOf<Int>()
    private lateinit var selectedCaff: GetAllCaffResponse
    private var isRunning = false
    private lateinit var adapter: CommentAdapter
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedCaff = UserData.getSelectedCaff()

        for(ciff in selectedCaff.caff?.ciffs!!){
            if(ciff != null){
                val h = ciff.height
                val w = ciff.width
                if(w != null && h != null){
                    var colorInt: IntArray = IntArray(w * h)
                    var j = 0
                    for(i in 0 until ciff.rgb_values?.size!! step 3){
                        //caff.caff.ciffs?.get(0)?.rgb_values!![i]/255
                        colorInt[j] = Color.rgb(
                            ciff.rgb_values!![i],
                            ciff.rgb_values!![i + 1],
                            ciff.rgb_values!![i + 2]
                        )
                        j++
                    }
                    val bitmap = Bitmap.createBitmap(colorInt, w, h, Bitmap.Config.ARGB_8888)
                    bitmaps.add(bitmap)
                    if(ciff.duration != null){
                        durs.add(ciff.duration!!)
                    }
                    else{
                        durs.add(0)
                    }

                }

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaffDetailsBinding.inflate(layoutInflater)

        adapter = CommentAdapter(ArrayList(selectedCaff.comments))

        binding.rvComments.adapter = adapter

        binding.btnSendComment.setOnClickListener {
            val text = binding.etCommentText.text
            val uname = UserData.getUserName()

            uploadComment(text.toString(),uname)
        }

        if(bitmaps.size > 0){
            updateIv(bitmaps[0])
        }

        binding.swipetorefreshDetails.setOnRefreshListener {
            getComments()
            binding.swipetorefreshDetails.isRefreshing = false
        }

        binding.ivDownloadCaffUser.setOnClickListener {
            downloadCaff()
        }

        binding.tvCaffDetailsTitle.text = selectedCaff.title
        binding.ivCaffDetails.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                Thread {

                    for (i in 0 until bitmaps.size) {
                        updateIv(bitmaps[i])
                        Thread.sleep(durs[i].toLong())
                    }

                    isRunning = false
                }.start()
            }
        }

        return binding.root
    }

    fun updateIv(bitmap: Bitmap){
        activity?.runOnUiThread {
            binding.ivCaffDetails.setImageBitmap(bitmap)
        }
    }

    private fun uploadComment(text: String, uname: String) {

        retrofit = RetrofitClient.getInstance()
        val jo = JsonObject()
        jo.addProperty("text", text)
        val fileP2 = RequestBody.create(MediaType.parse("application/json"), jo.toString())
        val call = retrofit.create(CommentService::class.java).addComment(fileP2, "Bearer " + UserData.getToken(), selectedCaff.id!!)

        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println("norip")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("rip")
                println(t.stackTraceToString())
            }
        })
    }

    private fun getComments(){
        retrofit = RetrofitClient.getInstance()
        val call = retrofit.create(CommentService::class.java).getCommentFromCaff("Bearer " + UserData.getToken(), UserData.getSelectedCaff().id!!)
        call.enqueue(object : Callback<List<GetCommentResponse>> {
            override fun onResponse(call: Call<List<GetCommentResponse>>, message: Response<List<GetCommentResponse>>) {
                if (message.code() == 200) {
                    println("yup")
                    adapter.updateData(message.body()!! as ArrayList<GetCommentResponse>)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<GetCommentResponse>>, t: Throwable) {
                println("nop")
                Toast.makeText(binding.root.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun downloadCaff(){
        retrofit = RetrofitClient.getFileInstance()
        val call = retrofit.create(CaffService::class.java).downloadCaff("Bearer " + UserData.getToken(), selectedCaff.id!!)

        call.enqueue(object: Callback<ResponseBody> {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("norip")

                val name = response.headers().get("Content-Disposition")?.substringAfter('\"')?.removeSuffix("\"")
                //println(response.body()?.string())
                saveFileToExternalStorage(name!!, response.body()!!.byteStream())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("rip")
                println(t.stackTraceToString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveFileToExternalStorage(name: String, body: InputStream): Uri {



        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context?.contentResolver
        var uri: Uri? = null

        try {
            uri = resolver?.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed to create new MediaStore record.")

            //FileOutputStream().use { stream -> stream.write(bytes) }

            resolver.openOutputStream(uri)?.use {
                it.write(body.readBytes())
            } ?: throw IOException("Failed to open output stream.")

            return uri

        } catch (e: IOException) {

            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                resolver?.delete(orphanUri, null, null)
            }

            throw e
        }
    }

}