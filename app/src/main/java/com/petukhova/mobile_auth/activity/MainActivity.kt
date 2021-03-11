package com.petukhova.mobile_auth.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.Repository
import com.petukhova.mobile_auth.Token
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity :
    AppCompatActivity() { // можно здесь указать ссылку на xml файл вместо setContentView

    var authenticated = false
    private val btnLog: Button by lazy { findViewById(R.id.btnLog) }
    private val btnReg: Button by lazy { findViewById(R.id.btnReg) }
    private val textInputLogin: TextInputEditText by lazy { findViewById(R.id.textInputLogin) }
    private val textInputPassword: TextInputEditText by lazy { findViewById(R.id.textInputPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ativity_main)
        checkAuth()
        btnLog.setOnClickListener { auth() }
        btnReg.setOnClickListener { registration() }

    }

    //Проверяем была ли аутентификация пользователя, чтобы при нажатии кнопки назад не попадать на повторную аутентифик-ю
    private fun checkAuth() {
        if (authenticated) { // Если флаг true, то повторно аутентификацию не проходим и отправляем пользователя в активиити постов
            val feedActivityIntent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(feedActivityIntent)
            finish()
        }
    }

    private fun checktextInput(): Boolean {
        if (textInputLogin.text.toString().isEmpty() || textInputPassword.text.toString().isEmpty()) {
            Toast.makeText(
                this@MainActivity,
                "Введите имя пользователя и пароль", Toast.LENGTH_LONG
            ).show()
            return false
        } else {
            return true
        }
    }

    private fun auth() {
        val login: String = textInputLogin.text.toString()
        val password: String = textInputPassword.text.toString()

        lifecycleScope.launch {
            if (checktextInput()) {
                val token = Repository.authenticate(login, password)

                if (token.isSuccessful) {
                    authenticated = true
                    Toast.makeText(
                        this@MainActivity,
                        "Успешная аутентификация",
                        Toast.LENGTH_SHORT
                    ).show()
                    val feedActivityIntent =
                        Intent(this@MainActivity, FeedActivity::class.java)
                    startActivity(feedActivityIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Неуспешная аутентификация",
                        Toast.LENGTH_LONG
                    ).show()


                }
            }
        }
    }

    private fun registration() {
        val userName: String = textInputLogin.text.toString()
        val passwordReg: String = textInputPassword.text.toString()

        lifecycleScope.launch {
            if (checktextInput()) {
                val token: Response<Token> = Repository.registration(userName, passwordReg)
                if (token.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Успешная регистрация",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Неуспешная регистрация",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }
    }
}



