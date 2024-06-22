package com.harish.artsense

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.harish.artsense.Api.Response.LoginData
import com.harish.artsense.ViewModel.MainViewModel
import com.harish.artsense.ViewModel.ViewModelFactory
import com.harish.artsense.ViewModel.WelcomeViewModel
import com.harish.artsense.databinding.ActivityMainBinding
import com.harish.artsense.databinding.ActivityWelcomeBinding
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener{
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
        }
        binding.tvGuestButton.setOnClickListener{
            lifecycleScope.launch {
                val guestUser = LoginData(
                    name = "Guest",
                    token = "",
                    userId = "Guest",
                    isLogin = true,
                    isGuest = true
                )
                viewModel.guestLogin(guestUser)
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()
            }
        }

    }
}