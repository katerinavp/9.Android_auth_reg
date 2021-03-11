package com.petukhova.mobile_auth.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.Repository
import com.petukhova.mobile_auth.Token
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity: AppCompatActivity() { // можно здесь указать ссылку на xml файл вместо setContentView

    var authenticated = false
    private lateinit var btnLog: Button
    private lateinit var btnReg: Button
    private lateinit var textInputLogin: TextInputLayout
    private lateinit var textInputPassword: TextInputLayout

    //можно объявить MainActivity, как CoroutineScope
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ativity_main)
        init()
        authAndReg()
    }

    private fun init() {
        btnLog = findViewById<Button>(R.id.btnLog)
        btnReg = findViewById<Button>(R.id.btnReg)
        textInputLogin = findViewById<TextInputLayout>(R.id.textInputLogin)
        textInputPassword = findViewById<TextInputLayout>(R.id.textInputPassword)
    }

    private fun authAndReg() {

        if (authenticated) { // Если флаг true, то повторно аутентификацию не проходим и отправляем пользователя в активиити постов
            val feedActivityIntent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(feedActivityIntent)
            finish()
        } else { // иначе отправляем пользователя ввести логин и пароль для аутентификации
            btnLog.setOnClickListener {

                lifecycleScope.launch {

                    if (textInputLogin.toString().isEmpty() || textInputPassword.toString()
                            .isEmpty()
                    ) {
                        Toast.makeText(
                            this@MainActivity, "Введите логин и пароль", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val login: String = textInputLogin.toString()
                        val password: String = textInputPassword.toString()
                        val token: Response<Token> = Repository.authenticate(login, password)

                        if (token.isSuccessful) {
                            authenticated = true
                            Toast.makeText(
                                this@MainActivity,
                                "Успешная авторизация",
                                Toast.LENGTH_SHORT
                            ).show()
                            val feedActivityIntent =
                                Intent(this@MainActivity, FeedActivity::class.java)
                            startActivity(feedActivityIntent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Неуспешная авторизация",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            btnReg.setOnClickListener {
                val registrationIntent = Intent(this@MainActivity, RegistrationActivity::class.java)
                startActivity(registrationIntent)
            }

        }
    }
}

