package com.ykstar.bangkit.taniland.pages.lahan

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityTambahLahanBinding
import com.ykstar.bangkit.taniland.models.LahanRequest
import com.ykstar.bangkit.taniland.pages.MainActivity
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.viewmodels.LahanViewModel

class TambahLahanActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private lateinit var binding: ActivityTambahLahanBinding
    private lateinit var viewModel: LahanViewModel
    private lateinit var userPreferences: UserPreference
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahLahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        userPreferences = UserPreference(this)
        viewModel = ViewModelProvider(this)[LahanViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupTextWatchers()

        binding.backButton.setOnClickListener(){
            finish()
        }
    }

    private fun setupTextWatchers() {
        binding.inputNamaLahan.addTextChangedListener(textWatcher)
        binding.inputLuasLahan.addTextChangedListener(textWatcher)
        binding.inputAlamat.addTextChangedListener(textWatcher)
        binding.buttonSimpan.isEnabled = false

        binding.buttonSimpan.setOnClickListener {
            progressDialog.show()

            val token = userPreferences.getToken()
            viewModel.tambahLahan(
                token, LahanRequest(
                    user_id = userPreferences.getUserID().toString(),
                    nama = binding.inputNamaLahan.text.toString(),
                    luas = binding.inputLuasLahan.text.toString().toDouble(),
                    alamat = binding.inputAlamat.text.toString(),
                    lat = lat,
                    lon = lon
                )
            )

            viewModel.lahanTambah.observe(this) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        Intent(this, MainActivity::class.java).also {
                            it.putExtra("menu", "menuLahan")
                            startActivity(it)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(this, resource.exception?.message, Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            binding.buttonSimpan.isEnabled = validateForm()
        }
    }

    private fun validateForm(): Boolean {
        return binding.inputNamaLahan.text.toString().isNotEmpty() &&
                binding.inputLuasLahan.text.toString().isNotEmpty() &&
                binding.inputAlamat.text.toString().isNotEmpty()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mMap.isMyLocationEnabled = true

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), 15f
                    )
                )
            }
        }

        mMap.setOnMapClickListener(this)
    }


    override fun onMapClick(point: LatLng) {
        marker?.remove()

        // Create marker options
        val markerOptions = MarkerOptions()
        markerOptions.position(point)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        // Adding marker on the map
        marker = mMap.addMarker(markerOptions)

        // Animate camera to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point))

        lat = marker?.position?.latitude
        lon = marker?.position?.longitude

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.maps) as SupportMapFragment
                mapFragment.getMapAsync(this)
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}