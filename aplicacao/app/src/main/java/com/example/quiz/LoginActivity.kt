package com.example.quiz

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LoginDatabase.getDatabase(applicationContext)
        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        // Verificação se o usuário está logado
        if (sharedPref.getBoolean("isLoggedIn", false)) {
            goToHome()
        }

        binding.inscreverse.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.button.setOnClickListener {
            val email = binding.email.text.toString()
            val senha = binding.senha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                lifecycleScope.launch {
                    val isValid = validateLogin(email, senha)
                    withContext(Dispatchers.Main) {
                        if (isValid) {
                            sharedPref.edit().putBoolean("isLoggedIn", true).apply()
                            showToast("Login bem-sucedido!")
                            goToHome()
                        } else {
                            showToast("E-mail ou senha inválidos!")
                        }
                    }
                }
            } else {
                showToast("Por favor, preencha todos os campos.")
            }
        }
    }

    private suspend fun validateLogin(email: String, senha: String): Boolean {
        return withContext(Dispatchers.IO) {
            db.loginDao().getLoginByEmailAndPassword(email, senha) != null
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
