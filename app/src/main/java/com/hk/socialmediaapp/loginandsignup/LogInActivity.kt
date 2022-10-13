package com.hk.socialmediaapp.loginandsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.hk.socialmediaapp.MainActivity
import com.hk.socialmediaapp.R
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.databinding.ActivityLogInBinding
import com.hk.socialmediaapp.responses.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

class  LogInActivity : AppCompatActivity() {
    lateinit var  binding: ActivityLogInBinding

//    private var retrofit: Retrofit? = null
//    private var retrofitInterface: RetrofitInterface? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        if(sessionManager.fetchAuthToken() != null){
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.doneBtn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
        if(email == "" || password == ""){
            Toast.makeText(this,"Please fill the details",Toast.LENGTH_SHORT).show()
        }else{
            login(email,password)
            }
        }

        binding.tvRegister.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }

    }

    private fun login(email: String,password: String){
        try {
            Log.d("hii",apiClient.toString())

            apiClient.getretrofitService(this)
                .login(email,password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "error1", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        Log.d("hii2",response.toString())
                        val loginResponse: LoginResponse? = response.body()
                        Log.d("hii3",loginResponse.toString())
                        if (response.isSuccessful) {
                            if (loginResponse != null) {
                                sessionManager.saveAuthToken(loginResponse.token)
                                Log.d("mainLog",loginResponse.toString())
                                Toast.makeText(applicationContext, loginResponse.token, Toast.LENGTH_SHORT).show()
                            }
                            Toast.makeText(applicationContext, "Logged in successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LogInActivity,MainActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}