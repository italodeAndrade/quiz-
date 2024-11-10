package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.quiz.ui.theme.QuizTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.Room

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
                val userName = intent.getStringExtra("userName") ?: "Jogador"

                NavHost(navController = navController, startDestination = "menu_screen") {
                    composable("menu_screen") {
                        MenuScreen(navController = navController, userName = userName)
                    }
                    composable("quiz_screen") {
                        QuizScreen(
                            questions = getQuestions(),
                            userDao = userDao,
                            userName = userName,
                            onFinishQuiz = { score ->
                                finishQuizAndNavigateBack(navController, userName, score)
                            }
                        )
                    }
                    composable("leaderboard_screen") {
                        LeaderboardScreen(userDao = userDao)
                    }
                }
            }
        }
    }

    private fun finishQuizAndNavigateBack(navController: NavController, userName: String, score: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                userDao.insert(User(name = userName, score = score))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        navController.navigate("menu_screen") {
            popUpTo("quiz_screen") { inclusive = true }
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
            ),
            Question(
                questionText = "Qual invenção é atribuída a Alexander Graham Bell?",
                options = listOf("Telefone", "Lâmpada", "Rádio", "Computador"),
                correctAnswer = "Telefone",
                imageResId = R.drawable.bell
            ),
            Question(
                questionText = "Qual é a fórmula química do gás carbônico?",
                options = listOf("CO2", "O2", "CH4", "H2O"),
                correctAnswer = "CO2",
                imageResId = R.drawable.molecula
            ),
        ).shuffled().map { question ->
            question.copy(options = question.options.shuffled())
        }
    }
}
