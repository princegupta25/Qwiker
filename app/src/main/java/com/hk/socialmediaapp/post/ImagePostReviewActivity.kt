package com.hk.socialmediaapp.post

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isInvisible
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.databinding.ActivityImagePostReviewBinding
import com.hk.socialmediaapp.utils.Constants
import java.io.File

class ImagePostReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImagePostReviewBinding
    var path=""
    var outputUri: Uri?=null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePostReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val path=intent.getStringExtra(Constants.FILE_PATH)
        var imgfile: File? = null

        imgfile= File(path)
        val mybitmap: Bitmap = BitmapFactory.decodeFile(imgfile.path);
        binding.galleryImageView.setImageBitmap(mybitmap)
        outputUri= Uri.fromFile(File(path))
//        editPhoto()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== RESULT_OK)
        {
            outputUri= data?.data!!
            binding.galleryImageView.setImageURI(outputUri)
            binding.progressBar.isInvisible
        }
    }

//    private fun editPhoto()
//    {
//        val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
//        dsPhotoEditorIntent.data = outputUri
//
//        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Vasukam Images")
//
//        val toolsToHide =
//            intArrayOf(DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP)
//
//        dsPhotoEditorIntent.putExtra(
//            DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
//            toolsToHide
//        )
//        startActivityForResult(dsPhotoEditorIntent, 200)
//    }
}