package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import com.example.quiz.ui.theme.QuizTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import androidx.room.Room

@OptIn(ExperimentalAnimationApi::class)
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
                val navController = rememberAnimatedNavController()
                val userName = intent.getStringExtra("userName") ?: "Jogador"

                AnimatedNavHost(
                    navController = navController,
                    startDestination = "menu_screen"
                ) {
                    composable(
                        route = "menu_screen",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { 1000 },
                                animationSpec = tween(500)
                            ) + fadeIn(animationSpec = tween(500))
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { -1000 },
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(300))
                        },
                        popEnterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { -1000 },
                                animationSpec = tween(500)
                            ) + fadeIn(animationSpec = tween(500))
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { 1000 },
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        MenuScreen(navController = navController, userName = userName)
                    }
                    composable(
                        route = "quiz_screen",
                        enterTransition = {
                            slideInVertically(
                                initialOffsetY = { 1000 },
                                animationSpec = tween(500)
                            ) + fadeIn(animationSpec = tween(500))
                        },
                        exitTransition = {
                            slideOutVertically(
                                targetOffsetY = { -1000 },
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        QuizScreen(
                            questions = getQuestions(),
                            userDao = userDao,
                            userName = userName,
                            onFinishQuiz = { score ->
                                finishQuizAndNavigateBack(navController, userName, score)
                            }
                        )
                    }
                    composable(
                        route = "leaderboard_screen",
                        enterTransition = {
                            scaleIn(
                                initialScale = 0.8f,
                                animationSpec = tween(500)
                            ) + fadeIn(animationSpec = tween(500))
                        },
                        exitTransition = {
                            scaleOut(
                                targetScale = 1.2f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
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
