package com.princeoprince.simplenote.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.princeoprince.simplenote.R
import com.princeoprince.simplenote.controller.InternalFileRepository
import com.princeoprince.simplenote.controller.NoteRepository

class MainActivity : AppCompatActivity() {

    private val repo: NoteRepository by lazy { InternalFileRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}