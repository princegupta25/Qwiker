package com.hk.socialmediaapp.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hk.socialmediaapp.MainActivity
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.databinding.ActivityCreateTextPostBinding

class CreateTextPost : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTextPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTextPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var title: String = binding.titlePostEt.text.toString()
        var desc: String = binding.descPostEt.text.toString()
        binding.nextBtn.setOnClickListener {
//            startActivity(Intent(thi
        //            s,TextPostActivity::class.java))
        }
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}