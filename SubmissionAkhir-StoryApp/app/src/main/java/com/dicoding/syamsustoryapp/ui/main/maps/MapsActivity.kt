package com.dicoding.syamsustoryapp.ui.main.maps

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.dicoding.syamsustoryapp.R
import com.dicoding.syamsustoryapp.data.Result
import com.dicoding.syamsustoryapp.data.model.ListStoryItem
import com.dicoding.syamsustoryapp.data.session.ViewModelFactory
import com.dicoding.syamsustoryapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import androidx.activity.result.contract.ActivityResultContracts


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private val viewModel: MapsViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            enableMyLocation()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupMapFragment()
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.maps)
    }

    private fun setupMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.apply {
            uiSettings.apply {
                isZoomControlsEnabled = true
                isIndoorLevelPickerEnabled = true
                isCompassEnabled = true
                isMapToolbarEnabled = true
            }
            setOnMapClickListener { latLng ->
                addMarker(latLng)
            }
            getStoryLocations(this)
            enableMyLocation()
        }
    }

    private fun getStoryLocations(googleMap: GoogleMap) {
        viewModel.getStoryMap().observe(this) { result ->
            when (result) {
                is Result.Success -> showStoryMarkers(result.data.listStory, googleMap)
                is Result.Error -> Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
                is Result.Loading -> {
                }
                else -> {
                }
            }
        }
    }

    private fun showStoryMarkers(stories: List<ListStoryItem>, googleMap: GoogleMap) {
        val boundsBuilder = LatLngBounds.builder()
        stories.forEach { story ->
            val position = LatLng(story.lat, story.lon)
            val markerOptions = MarkerOptions()
                .position(position)
                .title(story.name)
                .snippet(story.description)
            googleMap.addMarker(markerOptions)
            boundsBuilder.include(position)
        }
        val bounds = boundsBuilder.build()
        val padding = 16
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))

        googleMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            true
        }
    }

    private fun addMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Custom Marker")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mMap.addMarker(markerOptions)
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
