package com.jcgds.zuper

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jcgds.zuper.databinding.OperationsActivityBinding

class OperationsActivity : AppCompatActivity() {

    private val viewModel: OperationsViewModel by viewModels()

    private lateinit var binding: OperationsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OperationsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureView()
        initObservers()
    }

    private fun configureView() {
        // TODO: Configure RecyclerView
    }

    private fun initObservers() {
        viewModel.operations.observe(this) { operations ->
            Log.d("OperationsActivity", "Received new operations list: $operations")
        }
    }

}