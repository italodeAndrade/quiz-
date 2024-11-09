package com.example.quiz

import android.content.Context
import androidx.room.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val score: Int
)

@Entity(tableName = "leaderboard")
data class Leaderboard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val score: Int
)

data class Question(
    val questionText: String,
    val imageResId: Int,
    val options: List<String>,
    val correctAnswer: String
)

@Database(entities = [User::class, Leaderboard::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun leaderboardDao(): LeaderboardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user ORDER BY score DESC LIMIT 10")
    fun getTopUsers(): List<User>
}

@Dao
interface LeaderboardDao {
    @Insert
    fun insertLeaderboardEntry(entry: Leaderboard)

    @Query("SELECT * FROM leaderboard ORDER BY score DESC LIMIT 10")
    fun getTopScores(): List<Leaderboard>
}
