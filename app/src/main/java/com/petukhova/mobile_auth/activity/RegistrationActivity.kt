package com.petukhova.mobile_auth.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.petukhova.mobile_auth.Check
import com.petukhova.mobile_auth.R
import com.petukhova.mobile_auth.Repository
import com.petukhova.mobile_auth.Token
import isValid
import kotlinx.coroutines.launch
import retrofit2.Response
import splitties.activities.start
import splitties.toast.toast

class RegistrationActivity : AppCompatActivity() {
    private val btnSave: Button by lazy { findViewById(R.id.btnSave) }
    private val textInputUserName: TextInputEditText by lazy { findViewById(R.id.textInputUserName) }
    private val textInputPassword: TextInputEditText by lazy { findViewById(R.id.textInputPassword) }
    private val textInputPasswordRepeat: TextInputEditText by lazy { findViewById(R.id.textInputPasswordRepeat) }
    val check = Check()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnSave.setOnClickListener { registration() }
    }

    private fun registration() {
        val userName: String = textInputUserName.text.toString()
        val password: String = textInputPassword.text.toString()
        val passwordRepeat: String = textInputPasswordRepeat.text.toString()

        if (!check.checktextInputReg(
                userName,
                password,
                passwordRepeat
            )
        ) {//проверка на пустые поля ввода
            toast(R.string.enter_login_password)
        } else {
            if (!check.checkPassword(password, passwordRepeat)) { //проверка совпадают ли пароли
                toast(R.string.password_incorrect)
            } else {
                if (!isValid(password)) {
                    textInputPassword.error = getString(R.string.check_password_length)
                    textInputPasswordRepeat.error = getString(R.string.check_password_length)
                } else {
                    lifecycleScope.launch { // если поля ввода заполнены и пароли совпадают запускаем корутину и выполняем post запрос регистрации на сервер
                        try {
                            val token: Response<Token> = Repository.registration(userName, password)
                            if (token.isSuccessful) {
                                //с помощью splitties можно сократить toast
                                toast(R.string.success_reg)
                                goMainActivity()

                            } else {
                                toast(R.string.unsuccess_reg)
                            }
                        } catch (e: Exception) {
                            toast(R.string.turn_on_internet)
                        }
                    }
                }
            }
        }
    }


    private fun goMainActivity() {
        start<MainActivity>()
        finish()
    }
}