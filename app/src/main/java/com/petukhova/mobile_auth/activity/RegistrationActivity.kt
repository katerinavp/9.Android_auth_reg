package com.petukhova.mobile_auth.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.Repository
import com.petukhova.mobile_auth.Token
import kotlinx.coroutines.launch
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    private lateinit var btnSave: Button
    private lateinit var textInputUserName: TextInputLayout
    private lateinit var textInputPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        init()
        registration()
    }

    private fun init() {
        btnSave = findViewById<Button>(R.id.btnSave)
        textInputUserName = findViewById<TextInputLayout>(R.id.textInputUserName)
        textInputPassword = findViewById<TextInputLayout>(R.id.textInputPassword)
    }

    private fun registration() {
        btnSave.setOnClickListener {

            lifecycleScope.launch {
//                if (textInputUserName.toString().isEmpty() || textInputPassword.toString().isEmpty()
//                ) {
//                    Toast.makeText(
//                        this@RegistrationActivity,
//                        "Введите имя пользователя и пароль",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else {
                val userName: String = textInputUserName.toString()
                val passwordReg: String = textInputPassword.toString()
                val token: Response<Token> = Repository.registration(userName, passwordReg)
                if (token.isSuccessful) {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Успешная регистрация",
                        Toast.LENGTH_SHORT
                    ).show()
                    val regActivityIntent =
                        Intent(this@RegistrationActivity, MainActivity::class.java)
                    startActivity(regActivityIntent)
                    finish()
//                    }

                } else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Неуспешная регистрация",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }


        }
    }
}