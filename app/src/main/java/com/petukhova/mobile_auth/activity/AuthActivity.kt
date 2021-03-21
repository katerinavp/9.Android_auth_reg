package com.petukhova.mobile_auth.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.edit
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.petukhova.mobile_auth.*
import com.petukhova.mobile_auth.check.Check
import com.petukhova.mobile_auth.databinding.ActivityAuthBinding
import com.petukhova.mobile_auth.retrofit.Repository
import isValid
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

class MainActivity :
    AppCompatActivity() { // можно здесь указать ссылку на xml файл вместо setContentView
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAuthBinding.inflate(LayoutInflater.from(this))
    }

    var authenticated = false

    val check = Check()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (isAuthenticated()) {
            goFeedActivity()
        }

        binding.btnLog.setOnClickListener { auth() }
        binding.btnReg.setOnClickListener { goRegActivity() }

    }

    private fun auth() {
        val login: String = binding.textInputLogin.text.toString()
        val password: String = binding.textInputPassword.text.toString()
        if (!check.checktextInputAuth(login, password)) { //  проверка на пустые поля ввода
            toast(R.string.enter_login_password)
        } else {
            if (!isValid(password)) {
                binding.textInputPassword.error = getString(R.string.check_password_length)
            } else {
                binding.progressBar.isVisible =
                    true // если проверка прошла успешно запускаем прогерссбар и корутину для отправки post запроса аутентификации на сервер
                lifecycleScope.launch {

                    delay(5000)
                    try {
                        val token = Repository.authenticate(login, password)
                        binding.progressBar.isInvisible = true
                        if (token.isSuccessful) {
                            authenticated = true

                            setUserAuth(requireNotNull(token.body()).token) // 200 код

                            toast(R.string.success_auth)
                            goFeedActivity()

                        } else {
                            toast(R.string.Unsuccess_auth)
                        }
                    } catch (e: Exception) {
                        //с помощью splitties можно сократить
                        toast(R.string.turn_on_internet)
                        //вместо
//                    Toast.makeText(this@MainActivity, "Подключите интернет", Toast.LENGTH_LONG)
//                        .show()
                    }
                }
            }
        }
    }

    private fun goRegActivity() {
        //с помощью splitties можно сократить переход на другое активити
        start<RegistrationActivity>()
        finish()

    }

    private fun isAuthenticated() =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            //extension можно использовать, не нужно вызывать apply
            .edit { putString(AUTHENTICATED_SHARED_KEY, token) }
    //вместо
//            .edit()
//            .putString(AUTHENTICATED_SHARED_KEY, token)
//            .apply()


    private fun goFeedActivity() {

        //с помощью splitties можно сократить
        start<FeedActivity>()
        finish()
        //вместо
//        val feedActivityIntent =
//            Intent(this@MainActivity, FeedActivity::class.java)
//        startActivity(feedActivityIntent)
//        finish()
    }
}





