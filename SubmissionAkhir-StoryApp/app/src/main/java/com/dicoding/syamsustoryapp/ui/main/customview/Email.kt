package com.dicoding.syamsustoryapp.ui.main.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.syamsustoryapp.R

class Email : AppCompatEditText {

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
        setHint(R.string.email)
        addTextChangedListener(EmailTextWatcher())
    }

    private inner class EmailTextWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val email = s.toString().trim()
            error = when {
                email.isBlank() -> context.getString(R.string.peringatan_email)
                !email.isEmailValid() -> context.getString(R.string.email_check)
                else -> null
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    companion object {
        fun String.isEmailValid(): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }
    }
}
