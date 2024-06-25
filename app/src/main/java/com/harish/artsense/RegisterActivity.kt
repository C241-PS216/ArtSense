package com.harish.artsense

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.Api.Response.LoginResponse
import com.harish.artsense.ViewModel.LoginViewModel
import com.harish.artsense.ViewModel.RegisterViewModel
import com.harish.artsense.ViewModel.ViewModelFactory
import com.harish.artsense.databinding.ActivityRegisterBinding
import com.harish.artsense.di.ResultState
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ButtonHandle()
    }

    fun ButtonHandle(){
        binding.buttonRegister.setOnClickListener{
            val username =binding.usernameEditText.text.toString().trim()
            val password =binding.passwordEditText.text.toString().trim()
            Register(username, password)
        }
        binding.loginClickable.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    fun Register(username : String, password : String){
        if ((TextUtils.isEmpty(username)|| TextUtils.isEmpty(password))) {
            Toast.makeText(this, "Username and Password Cannot be empty", Toast.LENGTH_SHORT).show()
        }else {
            showLoading(true)
            viewModel.register(username, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Error -> {
                            showLoading(false)
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }

                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showLoading(false)
                            Toast.makeText(
                                this,
                                "Account ${result.data.username} Has Been Created",
                                Toast.LENGTH_SHORT
                            ).show()
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