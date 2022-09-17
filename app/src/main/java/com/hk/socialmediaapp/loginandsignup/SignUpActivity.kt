package com.hk.socialmediaapp.loginandsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityLogInBinding
import com.hk.socialmediaapp.databinding.ActivitySignUpBinding
import com.hk.socialmediaapp.responses.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception


class SignUpActivity : AppCompatActivity() {
    lateinit var  binding: ActivitySignUpBinding

    private var retrofit: Retrofit? = null
    private var retrofitInterface: RetrofitInterface? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)




        binding.doneBtn2.setOnClickListener {
//            if (email == "" || password == "" || name == "") {
//                Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show()
//            } else {
            val name = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            sessionManager.saveUserName(name)
            Log.d("signup",name)
            Log.d("signup",sessionManager.toString())
            val email = binding.etEmail.text.toString()
                signUp(email, password, name)
//            }
        }
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this,LogInActivity::class.java))
        }
    }

    private fun signUp(email: String,password: String,name: String){
        try {
            apiClient.getretrofitService(this)
                .signUp(name, email, password)
                .enqueue(object : Callback<SignUpResponse> {
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    ) {
                        val signUpResponse: SignUpResponse? = response.body()
                        Log.d("hii3",signUpResponse.toString())
                        if (response.isSuccessful) {
                            if (signUpResponse != null) {
                                userId = signUpResponse.user
                            }
                            Toast.makeText(applicationContext,"SignUp successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUpActivity,LogInActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Email already exists", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })

        } catch (e: Exception) {
        }
    }
}