package com.jeferson.moviestvtrivia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeferson.moviestvtrivia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvEpSelect.setOnClickListener {
            // TODO: alterar novamente para a intent intermediária
            //val intent = Intent(this, StartActivity::class.java)
            val intent = Intent(this, TimelineActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}