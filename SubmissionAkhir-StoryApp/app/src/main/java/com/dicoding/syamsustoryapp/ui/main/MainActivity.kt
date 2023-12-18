package com.dicoding.syamsustoryapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.paging.LoadState
import com.dicoding.syamsustoryapp.R
import com.dicoding.syamsustoryapp.data.session.LoginPreferences
import com.dicoding.syamsustoryapp.data.session.ViewModelFactory
import com.dicoding.syamsustoryapp.databinding.ActivityMainBinding
import com.dicoding.syamsustoryapp.ui.detail.AddStoryActivity
import com.dicoding.syamsustoryapp.ui.detail.DetailStoryActivity
import com.dicoding.syamsustoryapp.ui.main.adapter.StoryAdapter
import com.dicoding.syamsustoryapp.ui.main.maps.MapsActivity
import com.dicoding.syamsustoryapp.util.Constant.Companion.EXTRA_ID


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storyAdapter = StoryAdapter()
    private val loginPreferences by lazy { LoginPreferences(this) }
    private val viewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        observeStoryData()
        setupItemCLickListener()
        setupAddStoryButton()
        setupSwipeRefresh()
    }

    private fun initRecyclerView() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.red_200)
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        binding.swipeRefresh.isRefreshing = true
        mainViewModel.refreshStories()
    }


    private fun observeStoryData() {
        mainViewModel.getAllStory.observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }

        storyAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading ||
                loadState.source.refresh is LoadState.Error) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.swipeRefresh.isRefreshing = false
                }, 800)
            }
        }
    }

    private fun setupItemCLickListener() {
        storyAdapter.setOnItemClickCallback { data ->
            val intent = Intent(this@MainActivity, DetailStoryActivity::class.java).apply {
                putExtra(EXTRA_ID, data)
            }
            startActivity(intent)
        }
    }

    private fun setupAddStoryButton() {
        binding.addStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> showLogoutConfirmationDialog()
            R.id.maps -> startActivity(Intent(this, MapsActivity::class.java))
        }
        return true
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.alertTitle))
            .setMessage(getString(R.string.alertMessage))
            .setNegativeButton(getString(R.string.alertNegativ)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.alertPositive)) { _, _ ->
                loginPreferences.deleteUser()
                Toast.makeText(this, getString(R.string.logoutSuccess), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            .create()
            .show()
    }
}
