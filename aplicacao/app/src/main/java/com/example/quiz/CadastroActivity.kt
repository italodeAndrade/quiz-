package com.example.quiz

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LoginDatabase.getDatabase(applicationContext)

        // Máscara de data para `EditText` de Data de Nascimento
        binding.editTextDate.addTextChangedListener(DateMaskTextWatcher())

        binding.btReg.setOnClickListener {
            val email = binding.email.text.toString()
            val senha = binding.editTextText2.text.toString()
            val dataNascimento = binding.editTextDate.text.toString()

            if (validateRegistrationInput(email, senha, dataNascimento)) {
                val loginEntity = LoginEntity(email = email, senha = senha, dataNascimento = dataNascimento)
                lifecycleScope.launch {
                    insertLogin(loginEntity)
                }
            }
        }

        binding.lgRed.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateRegistrationInput(email: String, senha: String, dataNascimento: String): Boolean {
        return when {
            email.isEmpty() || senha.isEmpty() || dataNascimento.isEmpty() -> {
                showToast("Por favor, preencha todos os campos.")
                false
            }
            !isEmailValid(email) -> {
                showToast("E-mail inválido!")
                false
            }
            !isDateOfBirthValid(dataNascimento) -> {
                showToast("Data de nascimento inválida! Deve ser maior de 18 anos e não pode ser uma data futura.")
                false
            }
            else -> true
        }
    }

    private suspend fun insertLogin(loginEntity: LoginEntity) {
        withContext(Dispatchers.IO) {
            try {
                db.loginDao().insertLogin(loginEntity)
                withContext(Dispatchers.Main) {
                    showToast("Registro realizado com sucesso!")
                    clearFields()
                    goToLogin()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Erro ao registrar: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        binding.email.text.clear()
        binding.editTextText2.text.clear()
        binding.editTextDate.text.clear()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
