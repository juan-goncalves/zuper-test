package com.jcgds.zuper.features.operations

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcgds.zuper.databinding.OperationsActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OperationsActivity : AppCompatActivity() {

    private val viewModel by viewModels<OperationsViewModel>()
    private val operationsAdapter: OperationsAdapter = OperationsAdapter()
    private lateinit var binding: OperationsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OperationsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureView()
        initObservers()
    }

    private fun configureView() = with(binding) {
        operationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = operationsAdapter
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun initObservers() {
        viewModel.operations.observe(this) { operations ->
            operationsAdapter.data = operations
        }

        viewModel.showErrorState.observe(this) { shouldShow ->
            binding.errorState.visibility = if (shouldShow) View.VISIBLE else View.GONE
        }

        viewModel.showLoadingState.observe(this) { shouldShow ->
            binding.loadingState.visibility = if (shouldShow) View.VISIBLE else View.GONE
        }
    }

}