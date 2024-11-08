package com.example.quiz

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userName = intent.getStringExtra("userName") ?: "Usuário"
        val greetingTextView = findViewById<TextView>(R.id.greetingTextView)
        greetingTextView.text = "Bem-vindo, $userName!"

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
        }
    }
}
