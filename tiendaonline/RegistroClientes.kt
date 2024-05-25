package com.example.tiendaonline

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendaonline.database.DBmanager
import com.google.android.material.textfield.TextInputEditText

class RegistroClientes : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbManager: DBmanager
    private lateinit var textViewRegistros: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_clientes)

        sharedPreferences = getSharedPreferences("cliente_data", Context.MODE_PRIVATE)
        dbManager = DBmanager(this)

        textViewRegistros = findViewById(R.id.textViewRegistros)
        val etID = findViewById<TextInputEditText>(R.id.etID)
        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        btnRegistrar.setOnClickListener {
            val id = etID.text.toString().toIntOrNull()
            val nombre = etNombre.text.toString()

            if (id == null || nombre.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar los datos del cliente en SharedPreferences
            with(sharedPreferences.edit()) {
                putInt("cliente_id", id)
                putString("cliente_nombre", nombre)
                apply()
            }

            // Insertar cliente en la base de datos
            dbManager.open()
            dbManager.insertarCliente(id, nombre)
            dbManager.close()

            // Mostrar registros en TextView
            mostrarRegistros()

            Toast.makeText(this, "Cliente registrado exitosamente.", Toast.LENGTH_SHORT).show()
            etID.text?.clear()
            etNombre.text?.clear()
        }

        btnCancelar.setOnClickListener {
            etID.text?.clear()
            etNombre.text?.clear()
        }

        // Mostrar registros al iniciar la actividad
        mostrarRegistros()
    }

    private fun mostrarRegistros() {
        dbManager.open()
        val registros = dbManager.obtenerTodosClientes()
        dbManager.close()

        // Construir el texto con los registros
        val stringBuilder = StringBuilder()
        registros.forEachIndexed { index, cliente ->
            stringBuilder.append("Registro ${index + 1}: ID - ${cliente.id}, Nombre - ${cliente.nombre}\n")
        }

        // Mostrar el texto en el TextView
        textViewRegistros.text = stringBuilder.toString()
    }
}
