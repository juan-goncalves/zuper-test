package com.jcgds.zuper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jcgds.zuper.databinding.OperationsActivityBinding

class OperationsActivity : AppCompatActivity() {

    private lateinit var binding: OperationsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OperationsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}