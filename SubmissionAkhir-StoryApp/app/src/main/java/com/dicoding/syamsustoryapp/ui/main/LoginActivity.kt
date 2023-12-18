package com.dicoding.syamsustoryapp.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.syamsustoryapp.data.Result
import com.dicoding.syamsustoryapp.data.model.LoginResultModel
import com.dicoding.syamsustoryapp.data.model.LoginResponse
import com.dicoding.syamsustoryapp.data.session.LoginPreferences
import com.dicoding.syamsustoryapp.data.session.ViewModelFactory
import com.dicoding.syamsustoryapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val loginViewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            setContentView(root)
            setupUI()
        }
        supportActionBar?.hide()
    }

    private fun ActivityLoginBinding.setupUI() {
        showLoading(false)
        setupAnimation()
        loginButton.setOnClickListener { onLoginClicked() }
        registerButton.setOnClickListener { navigateToRegister() }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    private fun onLoginClicked() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            login(email, password)
        } else {
            showToast("Masukkan Email dan Password !")
        }
    }

    private fun login(email: String, password: String) {
        loginViewModel.postLogin(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    showLoading(false)
                    showToast("Login Gagal. Cek Email atau Password !")
                }
                is Result.Success -> {
                    successfulLogin(result.data)
                    showToast("Selamat Datang ${result.data.loginResult?.name}")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun successfulLogin(loginResponse: LoginResponse) {
        loginResponse.loginResult?.let { result ->
            val preferences = LoginPreferences(this)
            val loginResultModel = LoginResultModel(
                name = result.name,
                userId = result.userId,
                token = result.token
            )
            preferences.loginData = loginResultModel
        }
        navigateToHome()
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupAnimation() {
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(500),
                AnimatorSet().apply {
                    playTogether(
                        ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(500),
                        ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(500)
                    )
                },
                ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)
            )
            start()
        }
    }
}
