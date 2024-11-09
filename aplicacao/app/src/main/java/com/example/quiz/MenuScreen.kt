package com.example.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController, userName: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bem-vindo, $userName!",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = {
                navController.navigate("quiz_screen")
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Iniciar Quiz")
        }

        Button(
            onClick = {
                navController.navigate("leaderboard_screen")
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Ver Leaderboard")
        }
    }
}
