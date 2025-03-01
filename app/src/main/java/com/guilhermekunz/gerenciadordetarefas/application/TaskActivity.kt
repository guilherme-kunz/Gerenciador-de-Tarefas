package com.guilhermekunz.gerenciadordetarefas.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guilhermekunz.gerenciadordetarefas.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}