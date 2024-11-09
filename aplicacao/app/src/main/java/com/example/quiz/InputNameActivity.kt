package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.quiz.ui.theme.QuizTheme
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface


class InputNameActivity : ComponentActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).fallbackToDestructiveMigration()
            .build()

        setContent {
            QuizTheme {
                InputNameScreen(onNameSubmit = { name ->
                    saveNameToDatabase(name)
                })
            }
        }
    }

    private fun saveNameToDatabase(name: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (name.isNotBlank()) {
                    db.userDao().insert(User(name = name, score = 0))
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@InputNameActivity, MainActivity::class.java).apply {
                            putExtra("userName", name)
                        }
                        startActivity(intent)
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@InputNameActivity, "Nome inválido, por favor insira um nome!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@InputNameActivity, "Erro ao salvar o nome. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
                Log.e("InputNameActivity", "Erro ao salvar o nome: ${e.message}", e)
            }
        }
    }
}



@Composable
fun InputNameScreen(onNameSubmit: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bem-vindo ao Quiz App",
            fontSize = 24.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Um jogo de perguntas",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Light
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Digite seu nome") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onNameSubmit(name) },
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text(
                            text = "Enviar",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuizTheme {
        InputNameScreen(onNameSubmit = {})
    }
}