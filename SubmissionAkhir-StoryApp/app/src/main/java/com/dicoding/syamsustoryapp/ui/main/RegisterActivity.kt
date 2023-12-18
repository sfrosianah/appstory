package com.dicoding.syamsustoryapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import com.dicoding.syamsustoryapp.data.Result
import com.dicoding.syamsustoryapp.data.session.ViewModelFactory
import com.dicoding.syamsustoryapp.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(applicationContext)
    }
    private val registerViewModel: RegisterViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initListeners()
        showLoading(false)
        setupAnimation()
    }

    private fun initListeners() {
        binding.regisButton.setOnClickListener {
            validateAndSendDataUser()
        }
        binding.textViewLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun validateAndSendDataUser() {
        val name = binding.edName.text.toString().trim()
        val email = binding.edEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            processRegister(name, email, password)
        } else {
            Toast.makeText(this, "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearRegistrationForm() {
        with(binding) {
            edName.text?.clear()
            edEmail.text?.clear()
            edPassword.text?.clear()
        }
    }

    private fun processRegister(name: String, email: String, password: String) {
        registerViewModel.postRegister(name, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Terdapat Error !!!", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Akun Berhasil Didaftarkan !!! $name", Toast.LENGTH_LONG).show()
                    clearRegistrationForm()
                    navigateToLogin()
                }
            }
        }
    }


    private fun setupAnimation() {
        val imageAnimator = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val tvSignupAnimator = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(500)
        val edNameAnimator = ObjectAnimator.ofFloat(binding.nameTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val edEmailAnimator = ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val edPasswordAnimator = ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(500)
        val btnRegisterAnimator = ObjectAnimator.ofFloat(binding.regisButton, View.ALPHA, 1f).setDuration(500)
        val btnLoginAnimator = ObjectAnimator.ofFloat(binding.textViewLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                imageAnimator,
                tvSignupAnimator,
                AnimatorSet().apply {
                    playTogether(edNameAnimator, edEmailAnimator, edPasswordAnimator)
                },
                btnRegisterAnimator,
                btnLoginAnimator
            )
            start()
        }
    }

}
