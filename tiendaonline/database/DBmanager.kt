package com.example.tiendaonline.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBmanager(context: Context) {

    companion object {
        data class Cliente(val id: Int, val nombre: String)

        const val DB_NAME = "dbclientes"
        const val DB_VERSION = 1
        const val DB_TABLE = "Clientes"
        const val COL_ID = "ID"
        const val COL_NOMBRE = "Nombre"

        const val TABLA_CLIENTE_CREATE = "CREATE TABLE IF NOT EXISTS $DB_TABLE (" +
                "$COL_ID INTEGER PRIMARY KEY, " +
                "$COL_NOMBRE TEXT);"

        const val TABLA_CLIENTE_DROP = "DROP TABLE IF EXISTS $DB_TABLE"
    }

    private val dbHelper = DatabaseHelper(context)
    private var db: SQLiteDatabase? = null

    inner class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(TABLA_CLIENTE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL(TABLA_CLIENTE_DROP)
            onCreate(db)
        }
    }

    // Método para abrir la base de datos
    fun open() {
        db = dbHelper.writableDatabase
    }

    // Método para cerrar la base de datos
    fun close() {
        db?.close()
    }

    // Método para insertar un cliente
    fun insertarCliente(id: Int, nombre: String) {
        val contentValues = ContentValues()
        contentValues.put(COL_ID, id)
        contentValues.put(COL_NOMBRE, nombre)
        db?.insert(DB_TABLE, null, contentValues)
    }

    fun obtenerTodosClientes(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        val cursor = db?.query(DB_TABLE, null, null, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val idIndex = it.getColumnIndex(COL_ID)
                val nombreIndex = it.getColumnIndex(COL_NOMBRE)

                if (idIndex >= 0 && nombreIndex >= 0) {
                    val id = it.getInt(idIndex)
                    val nombre = it.getString(nombreIndex)
                    clientes.add(Cliente(id, nombre))
                }
            }
        }
        return clientes
    }



}
