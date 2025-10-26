package com.example.clubbam.data
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubbam.model.Actividad


const val DATABASE_NAME = "ClubBam.db"
const val DATABASE_VERSION = 1

class DBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS actividades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "descripcion TEXT," +
                "horario TEXT," +
                "cupo INTEGER," +
                "cuota REAL)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS actividades")
        onCreate(db)



    }

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


}