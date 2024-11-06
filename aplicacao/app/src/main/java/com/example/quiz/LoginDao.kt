package com.example.quiz

@Dao
interface LoginDao {
    @Insert
    suspend fun insertLogin(login: LoginEntity)

    @Query("SELECT * FROM login_table WHERE email = :email AND senha = :senha LIMIT 1")
    suspend fun getLoginByEmailAndPassword(email: String, senha: String): LoginEntity?
}
