package com.hk.socialmediaapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hk.UI.CreateFragment
import com.hk.UI.FeedFragment
import com.hk.UI.ProfileFragment
import com.hk.socialmediaapp.databinding.ActivityMainBinding
import com.hk.socialmediaapp.databinding.AddBottomSheetBinding
import com.hk.socialmediaapp.post.CreateImagePost
import com.hk.socialmediaapp.post.CreateTextPost

class MainActivity : AppCompatActivity() {

    private val createFragment = CreateFragment()
    private val feedFragment = FeedFragment()
    private val profileFragemnt = ProfileFragment()

    private lateinit var sheetBinding: AddBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sheetBinding = AddBottomSheetBinding.bind(
            LayoutInflater.from(this).inflate(R.layout.add_bottom_sheet,null)
        )
        bottomSheetDialog = BottomSheetDialog(
            this,R.style.BottomSheetDialogTheme
        )

        replaceFragment(feedFragment)

        binding.bottomNav.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.feed-> replaceFragment(feedFragment)
                R.id.create-> showBottomSheet()
                R.id.profile-> replaceFragment(profileFragemnt)
            }
            true
        }



    }

    private fun showBottomSheet() {
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.add_bottom_sheet,
            findViewById<ConstraintLayout>(R.id.addBottomSheetId)
        )
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        bottomSheetDialog.findViewById<CardView>(R.id.uploadText)!!.setOnClickListener {
            startActivity(Intent(this,CreateTextPost::class.java)
                .putExtra("Post_Type","TextPost"))

        }

        bottomSheetDialog.findViewById<CardView>(R.id.uploadImage)!!.setOnClickListener {
            startActivity(Intent(this,CreateImagePost::class.java)
                .putExtra("Post_Type","ImagePost"))

        }

    }






    fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.cancel()
        }
    }

}