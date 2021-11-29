package com.e.caffadminapp.Controller.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.e.caffadminapp.Network.Service.CaffService
import com.e.caffadminapp.model.AdminData
import com.e.szambizthfapplibrary.Utils.FileUtils
import com.e.szambizthfapplibrary.databinding.FragmentUploadBinding
import com.e.szambizthfapplibrary.network.RetrofitClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception

class UploadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val MY_REQUEST_CODE_PERMISSION = 1000
    private val MY_RESULT_CODE_FILECHOOSER = 2000

    private lateinit var retrofit: Retrofit
    private lateinit var binding: FragmentUploadBinding

    private val LOG_TAG = "AndroidExample"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadBinding.inflate(layoutInflater)
        binding.btnBrowseFile.setOnClickListener { askPermissionAndBrowseFile() }
        return binding.root
    }

    private fun askPermissionAndBrowseFile() {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Level 23

            // Check if we have Call permission
            val permisson = ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val permisson2 = ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permisson != PackageManager.PERMISSION_GRANTED || permisson2 != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_REQUEST_CODE_PERMISSION
                )
                return
            }
        }
        doBrowseFile()
    }

    private fun askPermission(f: String?, t: String) {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Level 23

            // Check if we have Call permission
            val permisson = ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (permisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_REQUEST_CODE_PERMISSION
                )
                return
            }
        }
        upload(f, t)
    }

    private fun doBrowseFile() {
        var chooseFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        chooseFileIntent.type = "*/*"
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file")
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER)
    }

    // When you have the request results

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            MY_REQUEST_CODE_PERMISSION -> {


                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i(LOG_TAG, "Permission granted!")
                    Toast.makeText(this.context, "Permission granted!", Toast.LENGTH_SHORT).show()
                    doBrowseFile()
                } else {
                    Log.i(LOG_TAG, "Permission denied!")
                    Toast.makeText(this.context, "Permission denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            MY_RESULT_CODE_FILECHOOSER -> if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val fileUri: Uri? = data.data
                    Log.i(LOG_TAG, "Uri: $fileUri")
                    var filePath: String? = null
                    try {
                        filePath = FileUtils.getPath(this.context, fileUri)
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "Error: $e")
                        Toast.makeText(this.context, "Error: $e", Toast.LENGTH_SHORT).show()
                    }
                    askPermission(filePath, binding.etPath.text.toString())

                }
            }
        }


    }

    fun getPath(): String? {
        return binding.etPath.text.toString()
    }

    private fun upload(filePath: String?, title: String) {

        if(filePath == null) return
        retrofit = RetrofitClient.getInstance()
        val f = File(filePath)
        //f.readBytes()
        val fileP1 = MultipartBody.Part.createFormData("caffFile", f.name, RequestBody.create(
            MediaType.parse("multipart/form-data"), f))
        val fileP2: RequestBody
        if(title.isEmpty()){
            fileP2 = RequestBody.create(MediaType.parse("multipart/form-data"), "Blank")
        }
        else{
            fileP2 = RequestBody.create(MediaType.parse("multipart/form-data"), title)
        }

        val call = retrofit.create(CaffService::class.java).addCaff(fileP1, fileP2, "Bearer " + AdminData.getToken())

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
}