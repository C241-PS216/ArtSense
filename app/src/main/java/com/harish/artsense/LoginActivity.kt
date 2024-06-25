package com.harish.artsense

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.Api.Response.LoginResponse
import com.harish.artsense.ViewModel.LoginViewModel
import com.harish.artsense.ViewModel.ViewModelFactory
import com.harish.artsense.databinding.ActivityLoginBinding
import com.harish.artsense.di.ResultState
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ButtonClick()
    }

    private fun ButtonClick(){
        binding.buttonLogin.setOnClickListener{
            val username =binding.emailEditText.text.toString().trim()
            val password =binding.passwordEditText.text.toString().trim()

            login(username, password)
        }
        binding.registerClickable.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    fun login(username: String, password : String){
        if ((TextUtils.isEmpty(username)|| TextUtils.isEmpty(password))){
            Toast.makeText(this, "Username and Password Cannot be empty", Toast.LENGTH_SHORT).show()
        }else{
            showLoading(true)
            viewModel.login(username,password).observe(this@LoginActivity){result->
                if (result != null){
                    when(result){
                        is ResultState.Error -> {
                            showLoading(false)
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showLoading(false)
                            lifecycleScope.launch {
                                showLoading(false)
                                val login =  LoginResponse(
                                    userid = result.data.userid
                                    , username = result.data.username
                                    , token = result.data.token
                                )
                                viewModel.saveSession(login)
                                LoginData(
                                    name =  result.data.username.toString(),
                                    token = result.data.token.toString(),
                                    userId = result.data.userid.toString(),
                                    isLogin = true,
                                    isGuest = false
                                )
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            }
                        }
                    }
                }

            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}