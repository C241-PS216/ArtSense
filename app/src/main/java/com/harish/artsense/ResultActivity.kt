package com.harish.artsense

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.harish.artsense.Api.Response.UploadData
import com.harish.artsense.ViewModel.ResultViewModel
import com.harish.artsense.ViewModel.ViewModelFactory
import com.harish.artsense.databinding.ActivityResultBinding
import com.harish.artsense.di.ResultState
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val detail : UploadData? =  intent.getParcelableExtra(EXTRA_DETAIL)

        val name = detail!!.nama

        binding.predictButton.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.tvArtist.text = name
        Glide.with(this)
            .load(detail.url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .override(2000, 2000)
            .centerCrop()
            .into(binding.uploadedImageView)

        getArtist(name)
        detail?.nama?.let { getArtist(it) }
    }

    fun getArtist(name : String){
            showLoading(true)
            viewModel.getArtist(name).observe(this@ResultActivity){result ->
                if (result != null) {
                    when(result){
                        is ResultState.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            showLoading(false)
                            displayLinks(result.data?.links)
                        }
                    }
                }
            }
        }


    private fun displayLinks(links: List<String?>?) {
        val container = binding.linkContainer
        container.removeAllViews() // Clear any existing views
        links?.forEach { link ->
            link?.let {
                val linkTextView = TextView(this).apply {
                    text = link
                    textSize = 16f
                    setTextColor(Color.GREEN)
                    setPadding(16, 16, 16, 16)
                    movementMethod = LinkMovementMethod.getInstance()
                    setOnClickListener {
                        openUrlInBrowser(link)
                    }
                }
                container.addView(linkTextView)
            }
        }
    }


    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }
}