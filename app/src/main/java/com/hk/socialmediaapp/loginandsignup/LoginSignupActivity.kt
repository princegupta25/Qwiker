package com.hk.socialmediaapp.loginandsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.hk.socialmediaapp.R
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hk.socialmediaapp.api.ApiClient
import com.hk.socialmediaapp.api.SessionManager
import com.hk.socialmediaapp.responses.LoginRequest
import com.hk.socialmediaapp.responses.LoginResponse
import com.hk.socialmediaapp.responses.SignUpRequest
import com.hk.socialmediaapp.responses.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import java.lang.Exception

class LoginSignupActivity : AppCompatActivity() {
    private var retrofit: Retrofit? = null
    private var retrofitInterface: RetrofitInterface? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        findViewById<View>(R.id.loginA).setOnClickListener { handleLoginDialog() }
        findViewById<View>(R.id.signupA).setOnClickListener { handleSignupDialog() }
    }

    private fun handleLoginDialog() {
        val view = layoutInflater.inflate(R.layout.login_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view).show()
        val loginBtn = view.findViewById<Button>(R.id.loginL)
        loginBtn.setOnClickListener {
            val emailET = view.findViewById<EditText>(R.id.emailEditL)
            val passwordET = view.findViewById<EditText>(R.id.passwordEditL)
            val email = emailET.text.toString()
            val password = passwordET.text.toString()
//            GlobalScope.launch(Dispatchers.Main) {
                try {
                    Log.d("hii",apiClient.toString())

                    apiClient.getretrofitService()
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
                                            Toast.makeText(applicationContext, loginResponse.token, Toast.LENGTH_SHORT).show()
                                        }
                                        Toast.makeText(applicationContext, "Logged in successfully", Toast.LENGTH_SHORT).show()
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

    private fun handleSignupDialog() {
        val view = layoutInflater.inflate(R.layout.signup_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view).show()
        val signupBtn = view.findViewById<Button>(R.id.signupS)
        signupBtn.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.nameEditS).text.toString()
            val email = view.findViewById<EditText>(R.id.emailEditS).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordEditS).text.toString()
//            GlobalScope.launch(Dispatchers.Main) {
                try {
                    apiClient.getretrofitService()
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
                                    Toast.makeText(applicationContext,"SignUp successfully",Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext, "Email already exists", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        })

                } catch (e: Exception) {
                }
//            }
        }
    }
}
