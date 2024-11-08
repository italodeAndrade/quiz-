package com.example.quiz

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla o layout usando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração de insets para ajustes de padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura o clique do botão para iniciar o quiz
        binding.startQuizButton.setOnClickListener {
            val playerName = binding.playerNameEditText.text.toString()

            if (playerName.isNotEmpty()) {
                // Salva o nome do jogador em SharedPreferences
                val sharedPref = getSharedPreferences("QuizPrefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("playerName", playerName)
                    apply()
                }

                // Inicia a atividade do quiz
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Por favor, insira seu nome.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
