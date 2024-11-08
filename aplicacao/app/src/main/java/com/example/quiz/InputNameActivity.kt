package com.example.quiz

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent

class InputNameActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_name)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()

            if (name.isNotEmpty()) {
                val user = User(name = name)

                GlobalScope.launch {
                    db.userDao().insert(user)

                    runOnUiThread {
                        val intent = Intent(this@InputNameActivity, MainActivity::class.java)
                        intent.putExtra("userName", name)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, insira um nome válido!", Toast.LENGTH_SHORT).show()
                nameEditText.requestFocus()

                if (name.isEmpty()) {
                    nameEditText.error = "Nome não pode ser vazio"
                }
            }
        }
    }
}
