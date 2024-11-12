package com.example.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.CardDefaults
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LeaderboardScreen(userDao: UserDao) {
    val leaderboard = remember { mutableStateOf<List<User>>(emptyList()) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        leaderboard.value = withContext(Dispatchers.IO) {
            userDao.getTopUsers()
        }
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) +
                    slideInVertically(initialOffsetY = { -40 })
        ) {
            Text(
                text = "Leaderboard",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (leaderboard.value.isEmpty()) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(500))
            ) {
                Text(text = "Nenhum jogador ainda.", fontSize = 18.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(leaderboard.value) { index, user ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val isHovered by interactionSource.collectIsHoveredAsState()
                    
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = index * 100
                            )
                        ) + slideInHorizontally(
                            initialOffsetX = { 100 },
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = index * 100
                            )
                        )
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .scale(if (isHovered) 1.02f else 1f)
                                .animateContentSize(),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (isHovered) 8.dp else 4.dp
                            )
                        ) {
                            LeaderboardItem(user = user, imageResId = R.drawable.question)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(user: User, imageResId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(text = user.name, fontSize = 18.sp)
            Text(text = "${user.score} pontos", fontSize = 18.sp)
        }
    }
}
