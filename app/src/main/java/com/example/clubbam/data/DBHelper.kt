package com.example.clubbam.data
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubbam.model.Actividad
import com.example.clubbam.model.Usuario


const val DATABASE_NAME = "ClubBam.db"
const val DATABASE_VERSION = 8

class DBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Habilitar claves foráneas
        db.execSQL("PRAGMA foreign_keys = ON")


        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "usuario TEXT UNIQUE," +
                "email TEXT UNIQUE," +
                "password TEXT)")

        // socio
        db.execSQL("CREATE TABLE IF NOT EXISTS socios (" +
                "nroCarnet INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "dni INTEGER," +
                "fechaNac TEXT," +            // YYYY-MM-DD
                "genero TEXT," +
                "mail TEXT," +
                "numCel TEXT," +
                "domicilio TEXT," +
                "aptoFisico INTEGER DEFAULT 1," + // 1=true
                "fechaIngreso TEXT," +        // YYYY-MM-DD
                "vencCuota TEXT," +           // YYYY-MM-DD
                "esActivo INTEGER DEFAULT 1," +
                "carnetEntregado INTEGER DEFAULT 1" +
                ")")

        // cuota
        db.execSQL("CREATE TABLE IF NOT EXISTS cuotas (" +
                "nroCuota INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nroSocio INTEGER," +
                "fechaVencimiento TEXT," +
                "fechaPago TEXT," +
                "importe REAL," +
                "metodoPago TEXT," +
                "cantCuotas INTEGER," +
                "FOREIGN KEY (nroSocio) REFERENCES socio(nroCarnet) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")")

        // actividad
        db.execSQL("CREATE TABLE IF NOT EXISTS actividades (" +
                "nroActividad INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "descripcion TEXT," +
                "costo REAL," +
                "dia TEXT," +
                "horario TEXT," +
                "cupos INTEGER" +
                ")")

        // noSocio
        db.execSQL("CREATE TABLE IF NOT EXISTS noSocios (" +
                "nroNoSocio INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "dni INTEGER," +
                "fechaNac TEXT," +            // YYYY-MM-DD
                "genero TEXT," +
                "mail TEXT," +
                "numCel TEXT," +
                "domicilio TEXT," +
                "aptoFisico INTEGER DEFAULT 1" +
                ")")

        // pagoActividad
        db.execSQL("CREATE TABLE IF NOT EXISTS pagoActividades (" +
                "nroPago INTEGER PRIMARY KEY AUTOINCREMENT," +
                "noSocio INTEGER," +
                "actividad INTEGER," +
                "monto REAL," +
                "fechaPago TEXT," +           // YYYY-MM-DD
                "FOREIGN KEY (noSocio) REFERENCES noSocios(nroNoSocio) ON DELETE CASCADE ON UPDATE CASCADE," +
                "FOREIGN KEY (actividad) REFERENCES actividades(nroActividad) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")")

        // INSERTS

        db.execSQL("INSERT INTO usuarios (nombre, apellido, usuario, email, password) VALUES " +
                "('Juan', 'Perez', 'admin', 'perezj@mail.com', 'elefante123')")
        db.execSQL("INSERT INTO socios (nroCarnet, nombre, apellido, dni, fechaNac, genero, numCel, domicilio, fechaIngreso, vencCuota) VALUES " +
                "(10001, 'Lucia', 'Pérez', 12345678, '1990-05-12', 'Femenino', '1123456789', 'Av. Siempre Viva 123', '2025-04-01', '2025-06-01')," +
                "(10002, 'Juan', 'Gómez', 27654321, '1985-09-23', 'Masculino', '1198765432', 'Calle Falsa 456', '2025-05-15', '2025-06-15')," +
                "(10003, 'Ana', 'Lopez', 23456789, '1992-03-08', 'Femenino', '1134567890', 'Pasaje 3 de Febrero', '2025-05-20', '2025-06-20')," +
                "(10004, 'Pedro', 'Ramirez', 34567891, '1978-12-30', 'Masculino', '1145678901', 'Barrio Centro', '2022-05-20', '2023-06-20')," +
                "(10005, 'Camila', 'Fernandez', 45678902, '2000-07-14', 'Femenino', '1156789012', 'Zona Norte', '2024-06-01', '2025-07-01')")

        db.execSQL("INSERT INTO cuotas (nroSocio, fechaVencimiento, fechaPago, importe, metodoPago, cantCuotas) VALUES " +
                "(10001, '2025-05-01', '2025-05-12', 15000, 'Efectivo', 1)," +
                "(10002, '2025-06-01', '2025-06-14', 15000, 'Tarjeta', 3)," +
                "(10003, '2025-06-20', '2025-06-15', 15000, 'Tarjeta', 1)," +
                "(10004, '2025-06-20', '2025-06-16', 150000, 'Efectivo', 1)," +
                "(10001, '2025-06-01', '2025-06-12', 15000, 'Efectivo', 1)")

        db.execSQL("INSERT INTO actividades (nombre, descripcion, costo, dia, horario, cupos) VALUES " +
                "('Yoga para Principiantes', 'Clase suave de yoga para todos los niveles', 2500, 'Lunes', '08:00', 20)," +
                "('Entrenamiento Funcional', 'Rutina completa de ejercicios de fuerza y cardio', 3500, 'Lunes', '18:00', 15)," +
                "('Stretching', 'Sesión de elongación para mejorar la flexibilidad', 3000, 'Lunes', '20:00', 12)," +
                "('Cardio Dance', 'Ejercicio aeróbico al ritmo de música moderna', 2100, 'Martes', '07:30', 25)," +
                "('Entrenamiento Funcional', 'Sesión intensa para mejorar fuerza y resistencia', 3500, 'Martes', '18:30', 15)," +
                "('Meditación Guiada', 'Relajación mental y control del estrés', 1900, 'Martes', '21:00', 10)," +
                "('Zumba', 'Clase de baile con música latina para quemar calorías', 2800, 'Miércoles', '19:00', 25)," +
                "('HIIT', 'Entrenamiento de intervalos de alta intensidad', 2600, 'Miércoles', '08:00', 20)," +
                "('Yoga Avanzado', 'Sesión para alumnos con experiencia previa', 2500, 'Miércoles', '20:30', 12)," +
                "('Pilates', 'Ejercicios de bajo impacto para fortalecer el core', 2500, 'Jueves', '09:00', 10)," +
                "('Entrenamiento Funcional', 'Entrenamiento en circuito con peso corporal', 3500, 'Jueves', '18:30', 15)," +
                "('Tai Chi', 'Disciplina suave para equilibrio y concentración', 2100, 'Jueves', '20:00', 18)," +
                "('Boxeo Recreativo', 'Técnicas básicas de boxeo sin contacto', 3700, 'Viernes', '17:00', 1)," +
                "('Stretching', 'Elongación muscular para finalizar la semana', 3000, 'Viernes', '08:30', 15)," +
                "('Ritmos Latinos', 'Clase de salsa y bachata para principiantes', 2200, 'Viernes', '19:30', 20)," +
                "('Yoga al Aire Libre', 'Clase matutina en el parque', 2500, 'Sábado', '09:00', 30)," +
                "('CrossFit Básico', 'Entrenamiento funcional con movimientos intensos', 3700, 'Sábado', '11:00', 12)," +
                "('Zumba Familiar', 'Baile para todas las edades', 2800, 'Sábado', '17:00', 25)")

        db.execSQL("INSERT INTO noSocios (nombre, apellido, dni, fechaNac, genero, numCel, domicilio) VALUES " +
                "('Juana', 'Pérez', 12345679, '1990-05-12', 'Femenino', '1123456759', 'Av. Siempre Viva 123')," +
                "('Mario', 'Gómez', 17654321, '1985-09-23', 'Masculino', '1198765422', 'Calle Falsa 456')," +
                "('Analia', 'Lopez', 23456789, 'Femenino', '1992-03-08', '1134567840', 'Pasaje 3 de Febrero 789')," + // ojo: orden correcto es FechaNac luego Genero
                "('Patricio', 'Ramirez', 24567890, '1978-12-30', 'Masculino', '1145676901', 'Barrio Centro 12')," +
                "('Jimena', 'Fernandez', 35678901, '2000-07-14', 'Femenino', '1156729012', 'Zona Norte 334')")

        db.execSQL("INSERT INTO pagoActividades (noSocio, actividad, monto, fechaPago) VALUES " +
                "(1, 1, 2000, '2025-06-15')," +
                "(2, 2, 2500, '2025-06-15')," +
                "(3, 3, 2200, '2025-06-17')," +
                "(4, 5, 2700, '2025-06-18')," +
                "(5, 4, 2300, '2025-06-19')")


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("PRAGMA foreign_keys = OFF")
        // borrar primero las que tienen FKs
        db.execSQL("DROP TABLE IF EXISTS pagoActividades")
        db.execSQL("DROP TABLE IF EXISTS cuotas")
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS noSocios")
        db.execSQL("DROP TABLE IF EXISTS socios")
        // despues las restantes
        db.execSQL("DROP TABLE IF EXISTS actividades")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("PRAGMA foreign_keys = ON")
        onCreate(db)

    }

    // TABLA ACTIVIDADES
    fun getActividades(): List<Actividad> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM actividades", null)
        val actividades = mutableListOf<Actividad>()
        while (cursor.moveToNext()) {
            val nroActividad = cursor.getInt(cursor.getColumnIndexOrThrow("nroActividad"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            val costo = cursor.getDouble(cursor.getColumnIndexOrThrow("costo"))
            val dia = cursor.getString(cursor.getColumnIndexOrThrow("dia"))
            val horario = cursor.getString(cursor.getColumnIndexOrThrow("horario"))
            val cupos = cursor.getInt(cursor.getColumnIndexOrThrow("cupos"))
            actividades.add(Actividad(nroActividad, nombre, descripcion, costo, dia, horario, cupos ))
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
            put("costo", actividad.costo)
            put("dia", actividad.dia)
            put("horario", actividad.horario)
            put("cupos", actividad.cupos)

        }
        val result = db.insert("actividades", null, values)
        db.close()
        return result
    }

    // TABLA USUARIOS
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

    // TABLA SOCIOS Y NO SOCIOS
    // devuelve NroCarnet generado
    fun registrarSocio(nombre: String, apellido: String, dni: Int, fechaNac: String,
                       genero: String, mail : String, numCel: String, domicilio: String, aptoFisico: Boolean,
                       fechaIngreso: String, vencCuota: String): Int {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("fechaNac", fechaNac)              // "YYYY-MM-DD"
            put("genero", genero)
            put("mail", mail)
            put("numCel", numCel)
            put("domicilio", domicilio)
            put("aptoFisico", if (aptoFisico) 1 else 0)
            put("fechaIngreso", fechaIngreso)
            put("vencCuota", vencCuota)
            put("esActivo", 1)
            put("carnetEntregado", 1)
        }
        val rowId = db.insert("socios", null, values)
        db.close()
        return if (rowId == -1L) -1 else rowId.toInt()
    }

    // devuelve NroNoSocio generado
    fun registrarNoSocio(nombre: String, apellido: String, dni: Int, fechaNac: String,
                         genero: String, mail : String, numCel: String, domicilio: String, aptoFisico: Boolean): Int {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("fechaNac", fechaNac)
            put("genero", genero)
            put("mail", mail)
            put("numCel", numCel)
            put("domicilio", domicilio)
            put("aptoFisico", if (aptoFisico) 1 else 0)
        }
        val rowId = db.insert("noSocios", null, values)
        db.close()
        return if (rowId == -1L) -1 else rowId.toInt()
    }

    // devuelve existe, esSocio, numero_identificador (nullable si no existe)
    fun verificarDni(dni: Int): Triple<Boolean, Boolean, Int?> {
        val db = this.readableDatabase

        // es socio?
        var cursor = db.rawQuery("SELECT nroCarnet FROM socios WHERE dni = ? LIMIT 1", arrayOf(dni.toString()))
        if (cursor.moveToFirst()) {
            val nro = cursor.getInt(cursor.getColumnIndexOrThrow("nroCarnet"))
            cursor.close()
            db.close()
            return Triple(true, true, nro)
        }
        cursor.close()

        // es no socio?
        cursor = db.rawQuery("SELECT nroNoSocio FROM noSocios WHERE dni = ? LIMIT 1", arrayOf(dni.toString()))
        if (cursor.moveToFirst()) {
            val nro = cursor.getInt(cursor.getColumnIndexOrThrow("nroNoSocio"))
            cursor.close()
            db.close()
            return Triple(true, false, nro)
        }
        cursor.close()

        db.close()
        return Triple(false, false, null)
    }

    // TABLA CUOTAS
    // devuelve id de la cuota (NroCuota)
    fun registrarCuota(nroSocio: Int, fechaVenc: String, fechaPago: String?,
                       importe: Double, metodoPago: String, cantCuotas: Int): Int {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("nroSocio", nroSocio)
            put("fechaVencimiento", fechaVenc)
            put("fechaPago", fechaPago) // puede ser null si aún no pagó
            put("importe", importe)
            put("metodoPago", metodoPago)
            put("cantCuotas", cantCuotas)
        }
        val rowId = db.insert("cuotas", null, values)
        db.close()
        return if (rowId == -1L) -1 else rowId.toInt()
    }


}