package com.princeoprince.simplenote.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.princeoprince.simplenote.databinding.ActivityMainBinding
import com.princeoprince.simplenote.controller.InternalFileRepository
import com.princeoprince.simplenote.controller.NoteRepository

class MainActivity : AppCompatActivity() {

    private val repo: NoteRepository by lazy { InternalFileRepository(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}