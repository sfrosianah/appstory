package com.dicoding.syamsustoryapp.ui.main.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.syamsustoryapp.R

class Nama : AppCompatEditText {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isEmpty()) error = resources.getString(R.string.peringatan_nama)
        }

        override fun afterTextChanged(s: Editable) {
        }
    }

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeView()
    }

    private fun initializeView() {
        apply {
            hint = context.getString(R.string.nama)
            addTextChangedListener(textWatcher)
        }
    }
}
