package com.dicoding.syamsustoryapp.ui.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.syamsustoryapp.R
import com.dicoding.syamsustoryapp.data.session.ViewModelFactory
import com.dicoding.syamsustoryapp.databinding.ActivityAddStoryBinding
import com.dicoding.syamsustoryapp.ui.main.MainActivity
import com.dicoding.syamsustoryapp.util.createTempFile
import com.dicoding.syamsustoryapp.util.reduceImageSize
import com.dicoding.syamsustoryapp.util.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private val viewModel: AddStoryViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var userLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        supportActionBar?.title = getString(R.string.upload_story)
        checkPermissionsAndRequestIfNecessary()
    }

    private fun checkPermissionsAndRequestIfNecessary() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupListeners() {
        binding.apply {
            btnCamera.setOnClickListener { openCamera() }
            btnGallery.setOnClickListener { openGallery() }
            swLocation.setOnCheckedChangeListener { _, isChecked -> handleLocationSwitch(isChecked) }
            btnUpload.setOnClickListener { uploadStory() }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this, "com.dicoding.syamsustoryapp", it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.addImage))
        launcherIntentGallery.launch(chooser)
    }

    private fun handleLocationSwitch(isChecked: Boolean) {
        if (isChecked) {
            getMyLastLocation()
        } else {
            userLocation = null
        }
    }

    private fun getMyLastLocation() {
        if (allPermissionsGranted()) {
            try {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    userLocation = location
                }.addOnFailureListener {
                    Toast.makeText(this, "Gagal Mendapatkan Lokasi!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: SecurityException) {
                Toast.makeText(this, "SecurityException: Failed to get location", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun uploadStory() {
        if (getFile == null) {
            Toast.makeText(this, getString(R.string.addImage), Toast.LENGTH_SHORT).show()
            return
        }

        val file = reduceImageSize(getFile!!)
        val description = binding.edAddDescription.text.toString().trim()
        if (description.isEmpty()) {
            binding.edAddDescription.error = getString(R.string.requiredDescription)
            return
        }

        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val latBody = userLocation?.latitude?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonBody = userLocation?.longitude?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

        uploadImage(imageMultipart, descriptionBody, latBody, lonBody)
    }

    private fun uploadImage(file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) {
        showLoading(true)
        viewModel.uploadImage(file, description, lat, lon).observe(this) {
            if (it != null) {
                redirectToMain()
                Toast.makeText(this, "Berhasil Mengupload Cerita", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal Mengupload Cerita", Toast.LENGTH_SHORT).show()
            }
            showLoading(false)
        }
    }

    private fun redirectToMain() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val bitmap = BitmapFactory.decodeFile(myFile.path)
            binding.previewImageView.setImageBitmap(bitmap)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.all { it.value }) {
            getMyLastLocation()
        } else {
            userLocation = null
            binding.swLocation.isChecked = false
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
