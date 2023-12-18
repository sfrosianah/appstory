package com.dicoding.syamsustoryapp.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.dicoding.syamsustoryapp.R
import com.dicoding.syamsustoryapp.data.session.LoginPreferences
import com.dicoding.syamsustoryapp.ui.main.LoginActivity
import com.dicoding.syamsustoryapp.ui.main.MainActivity
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")

class SplashScreenActivity : AppCompatActivity() {

    private val loginPreferences by lazy { LoginPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        navigateAfterDelay()
    }

    private fun navigateAfterDelay() {
        lifecycleScope.launchWhenStarted {
            delay(3000)
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        val loginResult = loginPreferences.loginData
        val intent = when {
            loginResult.name != null && loginResult.token != null && loginResult.userId != null -> {
                Intent(this, MainActivity::class.java)
            }
            else -> {
                Intent(this, LoginActivity::class.java)
            }
        }
        startActivity(intent)
        finish()
    }
}
