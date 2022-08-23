package app.thebitsolutions.filedownloader

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import app.thebitsolutions.filedownloader.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var binding: ActivityMainBinding
    private val downloadedvideo: MutableList<String> = mutableListOf()
    private val olddvideo: MutableList<VideoModel> = mutableListOf()
    private val newvideo: MutableList<VideoModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val array: ArrayList<String> = ArrayList<String>()


        newvideo.add(VideoModel("http://143.244.136.168/data/upload/f86256e1-62cc-405b-a709-569c6a76a66faa.mp4"))
        newvideo.add(VideoModel("http://143.244.136.168/data/upload/e0c19c87-46d0-4a1c-9392-9da6f589ffdb.mp4"))
        newvideo.add(VideoModel("http://143.244.136.168/data/upload/c33050e4-d2eb-4e70-b3b0-f3a56a1bf16e.mp4"))
        newvideo.add(VideoModel("http://143.244.136.168/data/upload/3c299bdc-94ba-4e80-a523-b2ce2df7d3e9.mp4"))

        array.add("http://143.244.136.168/data/upload/f86256e1-62cc-405b-a709-569c6a76a66f.mp4")
        array.add("http://143.244.136.168/data/upload/e0c19c87-46d0-4a1c-9392-9da6f589ffdb.mp4")
         array.add("http://143.244.136.168/data/upload/c33050e4-d2eb-4e70-b3b0-f3a56a1bf16e.mp4")
         array.add("http://143.244.136.168/data/upload/3c299bdc-94ba-4e80-a523-b2ce2df7d3e9.mp4")

        binding.getlist.setOnClickListener {

            GetDownloadedVideo()
            for (i in 0 until downloadedvideo.size) {
                val words = downloadedvideo.get(i).split("/")
                Log.e("File Name", words.last())
            }

        }
        binding.download.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
                for (i in 0 until array.size) {
                    downloadStart(array.get(i))
                    val words = array.get(i).split("/")
                    Log.e("New File Name", words.last())
                }
            }
        }

        binding.comare.setOnClickListener {
            val downloadedvidoes: ArrayList<VideoModel> = ArrayList<VideoModel>(GetDownloadedVideo())
            val newvidoes: ArrayList<VideoModel> = ArrayList<VideoModel>(newvideo)
            if(downloadedvidoes.size>0){
                for(info in downloadedvidoes){
                    var found = false
                    for (info2 in newvidoes){
                        if(info.name.split("/").last().equals(info2.name.split("/").last())){
                            found=true
                            Log.e("File","Same")
                        }
                    }
                    if(!found){
                        Log.e("File","Deleted")
                        DeleteFile(info.name)
                    }
                }
            }
        }
    }

    internal fun getDistinctStudents(newvideo: List<String>, downloaded: List<String>) {
        var listt : List<String> = newvideo+downloaded
        for (i in 0 until listt.size) {
            for (filename in newvideo) {
                if (!filename.contains(filename)) {
                    listt.drop(i)
                }
            }
        }
    }
    private fun downloadStart(downloadLink: String) {

        val downloadPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val dlManager = DownloadManager(downloadLink, downloadPath,
            object : OnDownloadProgressListener {
                override fun downloadStart() {
                    Log.e("TAG", "downloadStart")
                }

                override fun downloadedSuccess() {
                    Log.e("TAG", "downloadedSuccess")
                    binding.percentage.text = "download successful"
                }

                override fun downloadCancel() {
                    Log.e("TAG", "downloadCancel")

                }

                override fun downloadFail(error: String?) {
                    //loge("downloadFail:$error")
                    Log.e("TAG", "$error")

                }

                override fun percent(percent: Int) {
                    runOnUiThread {
                        // progressBar.progress=percent
                        binding.percentage.text = "$percent%"
                        //Log.e("TAG","$percent")
                    }

                }
            })

        dlManager.execute()


    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .setPositiveButton("agree", okListener)
            .setNegativeButton("cancel", null)
            .create()
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val writeExternalStorageAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (writeExternalStorageAccepted) {

                    Toast.makeText(
                        applicationContext,
                        "Allow Access external Storage",
                        Toast.LENGTH_LONG
                    ).show()
                    //downloadStart()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Deny Access external Storage",
                        Toast.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("you should access external storage",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            arrayOf(WRITE_EXTERNAL_STORAGE),
                                            PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        }
                    }

                }
            }
        }
    }


    fun DeleteFile(filename:String){
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) ,"/" + filename)
        if(file.exists()){
            file.delete()
        }
    }
        fun GetDownloadedVideo(): MutableList<VideoModel> {
            val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if(olddvideo.size>0){
                olddvideo.clear()
            }
            val pattern = ".mp4"
            val listFile = downloadPath.listFiles()
            if (listFile != null) {
                for (i in listFile.indices) {
                    val x = i
                    if (listFile[i].isDirectory) {
                        //walkdir(listFile[i])
                    } else {
                        if (listFile[i].name.endsWith(pattern)) {
                            // Do what ever u want, add the path of the video to the list
                            olddvideo!!.add(VideoModel(listFile[i].toString()))
                        }
                    }
                }
                Log.e("VIDEO",olddvideo!!.size.toString())
            }
            return olddvideo
        }
/*
        fun GetDownloadedVideo(): MutableList<String> {
            val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if(downloadedvideo.size>0){
                downloadedvideo.clear()
            }
            val pattern = ".mp4"
            val listFile = downloadPath.listFiles()
            if (listFile != null) {
                for (i in listFile.indices) {
                    val x = i
                    if (listFile[i].isDirectory) {
                        //walkdir(listFile[i])
                    } else {
                        if (listFile[i].name.endsWith(pattern)) {
                            // Do what ever u want, add the path of the video to the list
                            downloadedvideo!!.add(listFile[i].toString())
                        }
                    }
                }
                Log.e("VIDEO",downloadedvideo!!.size.toString())
            }
            return downloadedvideo
        }
*/

}