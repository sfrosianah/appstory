package com.dicoding.syamsustoryapp.ui.main.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.syamsustoryapp.R

class Password : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        hint = context.getString(R.string.password)
        addPasswordValidationTextWatcher()
    }

    private fun addPasswordValidationTextWatcher() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(password: Editable?) {
                validatePassword(password)
            }
        })
    }

    private fun validatePassword(password: Editable?) {
        val minimumPasswordLength = 8
        when {
            password.isNullOrEmpty() -> error = context.getString(R.string.peringatan_password)
            password.length < minimumPasswordLength -> error = context.getString(R.string.password_check)
        }
    }
}
