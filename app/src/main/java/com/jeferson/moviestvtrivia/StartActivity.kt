package com.jeferson.moviestvtrivia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeferson.moviestvtrivia.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvEpStart.setOnClickListener {
            val intent = Intent(this, TimelineActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}