package com.harish.artsense

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.harish.artsense.ViewModel.MainViewModel
import com.harish.artsense.ViewModel.ViewModelFactory
import com.harish.artsense.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val drawerLayout: DrawerLayout = binding.mainDrawer
        val navView: NavigationView = binding.navView

        val toolbar: Toolbar = binding.mainAppBar.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNav, R.string.closeNav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        toggle.drawerArrowDrawable.color = Color.WHITE
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.historyMenu ->{
                    startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
                }

                R.id.aboutMenu ->{
                    startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                }

                R.id.howItWorksMenu -> {
                    startActivity(Intent(this@MainActivity, HiwActivity::class.java))
                }
                R.id.loginMenu ->{
                    viewModel.getSession().observe(this@MainActivity){login ->
                        lifecycleScope.launch {
                            viewModel.logout()
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        }
                    }
                }

                R.id.logoutMenu -> {
                    viewModel.getSession().observe(this@MainActivity){login ->
                            lifecycleScope.launch {
                                Toast.makeText(applicationContext, "Logout Successfull", Toast.LENGTH_SHORT).show()
                                viewModel.logout()
                        }
                    }
                }
            }
            drawerLayout.closeDrawers()
            true
        }



        getSession()
    }

    private fun getSession(){
        viewModel.getSession().observe(this) { login ->
            if (!login.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{

                val navView: NavigationView = binding.navView
                val headerView = navView.getHeaderView(0)

                val headerTextViewUsername: TextView = headerView.findViewById(R.id.tvUsernameDrawer)
                val headerTextViewUserId: TextView = headerView.findViewById(R.id.tvUserIdDrawer)
                headerTextViewUsername.text = login.name
                headerTextViewUserId.text = login.userId

                if (!login.isGuest){
                navView.menu.findItem(R.id.loginMenu).isVisible = false
                }else{
                navView.menu.findItem(R.id.logoutMenu).isVisible = false
                }


            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}

