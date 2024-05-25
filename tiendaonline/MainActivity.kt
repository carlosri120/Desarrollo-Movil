package com.example.tiendaonline

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // Start Login Activity
       val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}
