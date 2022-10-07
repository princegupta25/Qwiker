package com.hk.socialmediaapp.post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.hk.UI.FeedFragment
import com.hk.socialmediaapp.MainActivity
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityCreateImagePostBinding
import com.hk.socialmediaapp.post.Adapter.Adapter
import com.hk.socialmediaapp.utils.Constants
import com.hk.socialmediaapp.utils.FilePaths
import com.hk.socialmediaapp.utils.FileSearch
import com.hk.socialmediaapp.utils.FileSearch.getDirectoryPaths
import java.io.File

class CreateImagePost : AppCompatActivity() {



    private lateinit var binding: ActivityCreateImagePostBinding

    private val CAMERA_REQUEST_CODE = 5
    private val GALLERY_REQUEST_CODE = 6
    private val MY_PERMISSIONS_REQUEST_CAMERA = 0
    private val MY_PERMISSIONS_REQUEST_READ_WRITE = 1

    private lateinit var directories: ArrayList<String>
    private val mAppend = "file:/"
    private var mSelectedImage: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateImagePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.isVisible
        directories = arrayListOf()



        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                MY_PERMISSIONS_REQUEST_READ_WRITE
            )
        } else {
            init()
        }

        binding.gallery.setOnClickListener {
            openGalleryForImage()
        }
        binding.camera.setOnClickListener {
              val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(cameraIntent.resolveActivity(this.packageManager) != null){
                startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE)
            }else{
                Toast.makeText(this,"Unable to open camera",Toast.LENGTH_SHORT).show()
            }
        }

        binding.nextBtn.setOnClickListener {

            val intent=Intent(this, ImagePostReviewActivity::class.java)
            intent.putExtra(Constants.FILE_PATH, mSelectedImage)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            with(binding) {
                galleryImageView.setImageURI(data?.data)

            } // handle chosen image
//            selected_image_uri = data?.data
            mSelectedImage = FileSearch.getRealPathFromURI(this, data?.data!!)

            val uri = Uri.fromFile(File(mSelectedImage))
//            Toast.makeText(this@CreateImagePost,uri.toString(),Toast.LENGTH_SHORT).show()

        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            with(binding) {

                galleryImageView.setImageBitmap(data?.extras?.get("data") as Bitmap)
            }

//            selected_image_uri = data?.data
//            mSelectedImage = FileSearch.getRealPathFromURI(this, data?.data!!)

        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun init() {

        directories.add("AllImages")
        directories.addAll(getDirectoryPaths(FilePaths.CAMERA))
        directories.addAll(getDirectoryPaths(FilePaths.PICTURES))

        val wpFolder = File(FilePaths.WHATSAPP)

        when {
            wpFolder.exists() -> {
                directories.add(FilePaths.WHATSAPP)
            }
        }

        val directoryNames: ArrayList<String> = ArrayList()
        for (i in directories.indices) {
            if (i != 0) {
                val index = directories[i].lastIndexOf("/")
                val string = directories[i].substring(index)
                directoryNames.add(string.drop(1))
            } else {
                directoryNames.add("All Images")
            }
        }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, directoryNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDirectory.adapter = adapter
        binding.spinnerDirectory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    setupGridView(directories[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

    }

    private fun setupGridView(selectedDirectory: String) {

        var imgURLs = ArrayList<String>()

        when (selectedDirectory) {
            "AllImages" -> {
                imgURLs = FileSearch.getAllImages(this)
            }
            else -> {
                imgURLs = FileSearch.getFilePaths(selectedDirectory)
            }
        }

//        binding.galaryImageRecView.setHasFixedSize(true)
//        val adapter = Adapter(this, imgURLs) {
//            //setImage(it, binding.galleryImageView, mAppend)
//            Glide.with(this).load(it).into(binding.galleryImageView)
//            mSelectedImage = it
////            selected_image_uri = Uri.fromFile(File(mSelectedImage))
//            val uri = Uri.fromFile(File(mSelectedImage))
////            Toast.makeText(this@CreateImagePost,uri.toString(),Toast.LENGTH_SHORT).show()
//
//        }
//        binding.galaryImageRecView.layoutManager = GridLayoutManager(this, 3)
//        binding.galaryImageRecView.adapter = adapter

        val adapter = Adapter(this,imgURLs){
            Glide.with(this).load(it).into(binding.galleryImageView)
            mSelectedImage = it
            val uri = Uri.fromFile(File(mSelectedImage))
        }
        binding.galaryImageRecView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.galaryImageRecView.adapter = adapter

        if (imgURLs.isNotEmpty()) {
            try {
                //setImage(imgURLs[0], binding.galleryImageView, mAppend)
                Glide.with(this).load(imgURLs[0]).into(binding.galleryImageView)
                mSelectedImage = imgURLs[0]
//                selected_image_uri = Uri.fromFile(File(mSelectedImage))
                val uri = Uri.fromFile(File(mSelectedImage))
//                Toast.makeText(this@CreateImagePost,uri.toString(),Toast.LENGTH_SHORT).show()

            } catch (e: ArrayIndexOutOfBoundsException) {
            }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }
}