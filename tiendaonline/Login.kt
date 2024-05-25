package com.example.tiendaonline

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class Login : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvLocation: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("com.example.tiendaonline.PREFS_NAME", Context.MODE_PRIVATE)

        tvLocation = findViewById(R.id.tvLocation)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val forgotPassword = findViewById<TextView>(R.id.forgot_password)
        forgotPassword.text = getString(R.string.register)
        forgotPassword.setOnClickListener {
            val intent = Intent(this, RegistroClientes::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(location.latitude, location.longitude, 1, object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: List<Address>) {
                                val city = addresses.firstOrNull()?.locality ?: getString(R.string.unknown)
                                tvLocation.text = getString(R.string.location_text, city, location.latitude, location.longitude)
                            }

                            override fun onError(errorMessage: String?) {
                                Toast.makeText(this@Login, R.string.unable_to_get_location, Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val city = addresses?.firstOrNull()?.locality ?: getString(R.string.unknown)
                        tvLocation.text = getString(R.string.location_text, city, location.latitude, location.longitude)
                    }
                } else {
                    Toast.makeText(this, R.string.unable_to_get_location, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, R.string.location_permission_not_granted, Toast.LENGTH_SHORT).show()
        }
    }
}
