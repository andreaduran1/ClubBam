package com.example.clubbam.data
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubbam.model.Usuario

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VERSION) {

    companion object{

        const val DATABASE_NAME = "ClubBam.db"
        const val DATABASE_VERSION = 1
        const val TABLE_USUARIO = "usuario"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_APELLIDO = "apellido"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_DOMICILIO = "domicilio"
        const val COLUMN_LOCALIDAD = "localidad"

        private const val SQL_CREATE_USUARIO = "CREATE TABLE $TABLE_USUARIO (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOMBRE TEXT," +
                "$COLUMN_APELLIDO TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_DOMICILIO TEXT," +
                "$COLUMN_LOCALIDAD TEXT)"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_USUARIO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Estrategia simple para desarrollo: borrar y recrear. En producción crear migraciones.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        onCreate(db)
    }

}