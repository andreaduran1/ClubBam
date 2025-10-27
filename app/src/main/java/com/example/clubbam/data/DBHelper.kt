package com.example.clubbam.data
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubbam.model.Actividad
import com.example.clubbam.model.Usuario


const val DATABASE_NAME = "ClubBam.db"
const val DATABASE_VERSION = 3

class DBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS actividades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "descripcion TEXT," +
                "horario TEXT," +
                "cupo INTEGER," +
                "cuota REAL)")

        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "usuario TEXT UNIQUE," +
                "email TEXT UNIQUE," +
                "password TEXT)")

        // Usuario de prueba
        db.execSQL("INSERT INTO usuarios (nombre, apellido, usuario, email, password) VALUES " +
                "('Juan', 'Perez', 'admin', 'perezj@mail.com', 'elefante123')")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)

    }

    // ---------------------------------------------------------
    // TABLA ACTIVIDADES
    // ---------------------------------------------------------
    fun getActividades(): List<Actividad> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM actividades", null)
        val actividades = mutableListOf<Actividad>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            val horario = cursor.getString(cursor.getColumnIndexOrThrow("horario"))
            val cupo = cursor.getInt(cursor.getColumnIndexOrThrow("cupo"))
            val cuota = cursor.getDouble(cursor.getColumnIndexOrThrow("cuota"))
            actividades.add(Actividad(id, nombre, descripcion, horario, cupo, cuota))
        }
        cursor.close()
        db.close()
        return actividades

    }
    fun insertActividad(actividad: Actividad): Long {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("nombre", actividad.nombre)
            put("descripcion", actividad.descripcion)
            put("horario", actividad.horario)
            put("cupo", actividad.cupo)
            put("cuota", actividad.cuota)
        }
        val result = db.insert("actividades", null, values)
        db.close()
        return result
    }

    // ---------------------------------------------------------
    // TABLA USUARIOS
    // ---------------------------------------------------------

    fun insertUsuario(usuario: Usuario): Long {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("nombre", usuario.nombre)
            put("apellido", usuario.apellido)
            put("usuario", usuario.usuario)
            put("email", usuario.email)
            put("password", usuario.password)
        }
        val result = db.insert("usuarios", null, values)
        db.close()
        return result
    }

    fun checkLogin(usuario: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND password = ?", arrayOf(usuario, password))
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }

    fun getUsuarioPorUsuario(nombreUsuario: String): Usuario? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", arrayOf(nombreUsuario))
        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            val user = cursor.getString(cursor.getColumnIndexOrThrow("usuario"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            usuario = Usuario(id, nombre, apellido, user, email, password)
        }
        cursor.close()
        db.close()
        return usuario
    }


}