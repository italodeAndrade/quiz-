package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.quiz.ui.theme.QuizTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "quiz-database"
        ).build()

        userDao = db.userDao()

        setContent {
            QuizTheme {
                val navController = rememberNavController()
                val userName = remember { mutableStateOf("Jogador") }

                LaunchedEffect(Unit) {
                    userName.value = getUserName()
                }

                NavHost(navController = navController, startDestination = "menu_screen") {
                    composable("menu_screen") {
                        MenuScreen(navController, userName.value)
                    }
                    composable("quiz_screen") {
                        QuizScreen(questions = getQuestions(), userDao = userDao, onFinishQuiz = { score ->
                            saveScore(userName.value, score)
                        })
                    }
                    composable("leaderboard_screen") { LeaderboardScreen(userDao = userDao) }
                }
            }
        }
    }

    private suspend fun getUserName(): String {
        return withContext(Dispatchers.IO) {
            val user = userDao.getTopUsers().firstOrNull()
            user?.name ?: "Jogador"
        }
    }

    private fun saveScore(name: String, score: Int) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    userDao.insert(User(name = name, score = score))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getQuestions(): List<Question> {
        return listOf(
            Question(
                questionText = "Qual é a capital da França?",
                options = listOf("Paris", "Londres", "Berlim", "Roma"),
                correctAnswer = "Paris",
                imageResId = R.drawable.map_france
            ),
            Question(
                questionText = "Qual é o maior planeta do sistema solar?",
                options = listOf("Terra", "Júpiter", "Saturno", "Marte"),
                correctAnswer = "Júpiter",
                imageResId = R.drawable.jupiter
            )
        ).shuffled()
            .map { question ->
                question.copy(options = question.options.shuffled())
            }
    }

}
