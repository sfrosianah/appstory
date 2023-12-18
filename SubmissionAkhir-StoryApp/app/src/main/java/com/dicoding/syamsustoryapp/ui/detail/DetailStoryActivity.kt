package com.dicoding.syamsustoryapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.syamsustoryapp.data.model.ListStoryItem
import com.dicoding.syamsustoryapp.databinding.ActivityDetailStoryBinding
import com.dicoding.syamsustoryapp.util.Constant.Companion.EXTRA_ID
import java.text.SimpleDateFormat
import java.util.*

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDetail()
        displayCurrentTime()
    }

    private fun displayCurrentTime() {
        val currentTime = Calendar.getInstance().time
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm:ss", Locale.getDefault())
        val formattedTime = outputFormat.format(currentTime)
        binding.tvDate.text = formattedTime
    }

    private fun getDetail() {
        val getData: ListStoryItem? = intent.getParcelableExtra(EXTRA_ID)
        if (getData != null) {
            binding.apply {
                tvname.text = getData.name
                tvDate.text = getData.createdAt
                tvDesc.text = getData.description
                Glide.with(this@DetailStoryActivity)
                    .load(getData.photoUrl)
                    .into(ivHero)
            }
        } else {
            finish()
        }
    }

}