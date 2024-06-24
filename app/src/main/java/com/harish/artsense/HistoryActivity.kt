package com.harish.artsense

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.harish.artsense.Api.adapter.HistoryAdapter
import com.harish.artsense.ViewModel.HistoryViewModel
import com.harish.artsense.ViewModel.MainViewModel
import com.harish.artsense.ViewModel.ViewModelFactory
import com.harish.artsense.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.setHasFixedSize(true)

       val adapter = HistoryAdapter()
           binding.rvHistory.adapter = HistoryAdapter()

        viewModel.Story.observe(this, {
            if (adapter.itemCount == 0){
                binding.ivNoHistory.visibility = View.VISIBLE
            }else{
            adapter.submitData(lifecycle, it)
            }
        })

    }
}