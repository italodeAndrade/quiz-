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

@Composable
fun QuizScreen(questions: List<Question>, userDao: UserDao, onFinishQuiz: (Int) -> Unit) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var name by remember { mutableStateOf("") }
    val question = questions[currentQuestionIndex]
    val options = question.options.shuffled()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = question.questionText, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        options.forEach { option ->
            Button(
                onClick = {
                    if (option == question.correctAnswer) {
                        score += 10
                    }
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                    } else {
                        coroutineScope.launch {
                            userDao.insert(User(name = name, score = score))
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




