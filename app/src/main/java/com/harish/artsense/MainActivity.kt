package com.harish.artsense

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.harish.artsense.Api.Response.UploadData
import com.harish.artsense.ViewModel.MainViewModel
import com.harish.artsense.ViewModel.ViewModelFactory
import com.harish.artsense.databinding.ActivityMainBinding
import com.harish.artsense.di.ResultState
import com.harish.artsense.di.reduceFileImage
import com.harish.artsense.di.uriToFile
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var currentImageUri: Uri? = null


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

        val uploadBtn: ImageView = findViewById(R.id.uploadButton)

        uploadBtn.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        val upload: Button = findViewById(R.id.upload)
        upload.visibility = View.GONE
        upload.setOnClickListener {
            uploadImage()
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


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            lifecycleScope.launch {
                showLoading(true)
                viewModel.Upload(imageFile).observe(this@MainActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Error -> {
                                showToast(result.error)
                                showLoading(false)
                            }
                            ResultState.Loading -> {
                                showLoading(true)
                            }
                            is ResultState.Success -> {
                                showLoading(false)
                                val data =  UploadData(
                                    nama = result.data.artist!!.nama!!,
                                    url = result.data.url!!
                                )
                                result(data)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun result(result : UploadData){
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(EXTRA_DETAIL, result)
        this.startActivity(intent)
    }

    private fun showImage() {
        currentImageUri?.let {
            val uploadBtn: ImageView = findViewById(R.id.uploadButton)
            Log.d("Image URI", "showImage: $it")
            uploadBtn.setImageURI(it)
            val upload: Button = findViewById(R.id.upload)
            upload.visibility = View.VISIBLE

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        val progress : ProgressBar = findViewById(R.id.progressIndicator1)
        progress.visibility =  if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }
}

