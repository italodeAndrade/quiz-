package com.example.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun MenuScreen(navController: NavController, userName: String) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)) +
                    slideInVertically(initialOffsetY = { -40 })
        ) {
            Text(
                text = "Bem-vindo, $userName!",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        val buttonSpecs = listOf(
            "quiz_screen" to "Iniciar Quiz",
            "leaderboard_screen" to "Ver Leaderboard"
        )

        buttonSpecs.forEachIndexed { index, (route, text) ->
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 500 + (index * 200))) +
                        scaleIn(initialScale = 0.8f)
            ) {
                Button(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .scale(if (isPressed) 0.95f else 1f),
                    interactionSource = interactionSource
                ) {
                    Text(text = text)
                }
            }
        }
    }
}
