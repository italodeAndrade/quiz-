package com.example.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CardDefaults


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LeaderboardScreen(userDao: UserDao) {
    val leaderboard = remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(true) {
        leaderboard.value = withContext(Dispatchers.IO) {
            userDao.getTopUsers()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Leaderboard",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (leaderboard.value.isEmpty()) {
            Text(text = "Nenhum jogador ainda.", fontSize = 18.sp)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(leaderboard.value) { user ->
                    LeaderboardItem(user = user, imageResId = R.drawable.question)
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

