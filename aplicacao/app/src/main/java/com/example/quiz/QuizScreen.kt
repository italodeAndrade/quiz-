package com.example.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun QuizScreen(questions: List<Question>, userDao: UserDao, userName: String, onFinishQuiz: (Int) -> Unit) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    val question = questions[currentQuestionIndex]
    val options = question.options.shuffled()
    val coroutineScope = rememberCoroutineScope()
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = question.imageResId),
            contentDescription = "Imagem da pergunta",
            modifier = Modifier.size(200.dp).padding(bottom = 16.dp)
        )

        Text(text = question.questionText, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        options.forEach { option ->
            Button(
                onClick = {
                    val timeTaken = (System.currentTimeMillis() - startTime) / 1000

                    val timeBasedScore = when {
                        timeTaken <= 5 -> 15
                        timeTaken <= 10 -> 12
                        timeTaken <= 15 -> 10
                        timeTaken <= 20 -> 8
                        timeTaken <= 30 -> 5
                        else -> 1
                    }

                    if (option == question.correctAnswer) {
                        score += timeBasedScore
                    }

                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                        startTime = System.currentTimeMillis()
                    } else {
                        coroutineScope.launch {
                            userDao.insert(User(name = userName, score = score))
                        }
                        onFinishQuiz(score)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text(text = option)
            }
        }
    }
}





