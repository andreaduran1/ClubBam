package com.example.clubbam.data
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.clubbam.model.Actividad
import com.example.clubbam.model.Usuario
import com.example.clubbam.model.Socio
import com.example.clubbam.model.Cuota
import java.time.LocalDate




const val DATABASE_NAME = "ClubBam.db"
const val DATABASE_VERSION = 14

class DBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON")

        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "usuario TEXT UNIQUE," +
                "email TEXT UNIQUE," +
                "password TEXT)")

        db.execSQL("CREATE TABLE IF NOT EXISTS socios (" +
                "nroCarnet INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "dni INTEGER," +
                "fechaNac TEXT," +
                "genero TEXT," +
                "mail TEXT," +
                "numCel TEXT," +
                "domicilio TEXT," +
                "aptoFisico INTEGER DEFAULT 1," +
                "fechaIngreso TEXT," +
                "vencCuota TEXT," +
                "esActivo INTEGER DEFAULT 1," +
                "carnetEntregado INTEGER DEFAULT 1" +
                ")")

        db.execSQL("CREATE TABLE IF NOT EXISTS cuotas (" +
                "nroCuota INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nroSocio INTEGER," +
                "fechaVencimiento TEXT," +
                "fechaPago TEXT," +
                "importe REAL," +
                "metodoPago TEXT," +
                "cantCuotas INTEGER," +
                "FOREIGN KEY (nroSocio) REFERENCES socios(nroCarnet) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")")

        db.execSQL("CREATE TABLE IF NOT EXISTS actividades (" +
                "nroActividad INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "descripcion TEXT," +
                "costo REAL," +
                "dia TEXT," +
                "horario TEXT," +
                "cupos INTEGER" +
                ")")

        db.execSQL("CREATE TABLE IF NOT EXISTS noSocios (" +
                "nroNoSocio INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "dni INTEGER," +
                "fechaNac TEXT," +
                "genero TEXT," +
                "mail TEXT," +
                "numCel TEXT," +
                "domicilio TEXT," +
                "aptoFisico INTEGER DEFAULT 1" +
                ")")

        db.execSQL("CREATE TABLE IF NOT EXISTS pagoActividades (" +
                "nroPago INTEGER PRIMARY KEY AUTOINCREMENT," +
                "noSocio INTEGER," +
                "actividad INTEGER," +
                "monto REAL," +
                "fechaPago TEXT," +
                "FOREIGN KEY (noSocio) REFERENCES noSocios(nroNoSocio) ON DELETE CASCADE ON UPDATE CASCADE," +
                "FOREIGN KEY (actividad) REFERENCES actividades(nroActividad) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")")

        // INSERTS
        db.execSQL("INSERT INTO usuarios (nombre, apellido, usuario, email, password) VALUES " +
                "('Juan', 'Perez', 'admin', 'perezj@mail.com', 'elefante123')")

        db.execSQL("INSERT INTO socios (nroCarnet, nombre, apellido, dni, fechaNac, genero, mail, numCel, domicilio, aptoFisico, fechaIngreso, vencCuota) VALUES " +
                "(10001, 'Lucia', 'Pérez', 12345678, '1990-05-12', 'Femenino', 'lucia@mail.com', '1123456789', 'Av. Siempre Viva 123', 1, '2025-04-01', '2025-06-01')," +
                "(10002, 'Juan', 'Gómez', 27654321, '1985-09-23', 'Masculino', 'juan@mail.com', '1198765432', 'Calle Falsa 456', 1, '2025-05-15', '2025-06-15')," +
                "(10003, 'Ana', 'Lopez', 23456789, '1992-03-08', 'Femenino', 'ana@mail.com', '1134567890', 'Pasaje 3 de Febrero', 1, '2025-05-20', '2025-06-20')," +
                "(10004, 'Pedro', 'Ramirez', 34567891, '1978-12-30', 'Masculino', 'pedro@mail.com', '1145678901', 'Barrio Centro', 1, '2022-05-20', '2023-06-20')," +
                "(10005, 'Camila', 'Fernandez', 45678902, '2000-07-14', 'Femenino', 'camila@mail.com', '1156789012', 'Zona Norte', 1, '2024-06-01', '2025-07-01')")

        db.execSQL("INSERT INTO cuotas (nroSocio, fechaVencimiento, fechaPago, importe, metodoPago, cantCuotas) VALUES " +
                "(10001, '2025-05-01', '2025-05-12', 15000, 'Efectivo', 1)," +
                "(10002, '2025-06-01', '2025-06-14', 15000, 'Tarjeta', 3)," +
                "(10003, '2025-06-20', '2025-06-15', 15000, 'Tarjeta', 1)," +
                "(10004, '2025-06-20', '2025-06-16', 15000, 'Efectivo', 1)," +
                "(10001, '2025-06-01', '2025-06-12', 15000, 'Efectivo', 1)")

        db.execSQL("INSERT INTO actividades (nombre, descripcion, costo, dia, horario, cupos) VALUES " +
                "('Yoga para Principiantes', 'Clase suave de yoga para todos los niveles', 2500, 'Lunes', '08:00', 20)," +
                "('Entrenamiento Funcional', 'Rutina completa de ejercicios de fuerza y cardio', 3500, 'Lunes', '18:00', 15)," +
                "('Stretching', 'Sesión de elongación para mejorar la flexibilidad', 3000, 'Lunes', '20:00', 12)," +
                "('Cardio Dance', 'Ejercicio aeróbico al ritmo de música moderna', 2100, 'Martes', '07:30', 25)," +
                "('Entrenamiento Funcional', 'Sesión intensa para mejorar fuerza y resistencia', 3500, 'Martes', '18:30', 15)," +
                "('Meditación Guiada', 'Relajación mental y control del estrés', 1900, 'Martes', '21:00', 1)")

        db.execSQL("INSERT INTO noSocios (nombre, apellido, dni, fechaNac, genero, mail, numCel, domicilio, aptoFisico) VALUES " +
                "('Juana', 'Pérez', 12345679, '1990-05-12', 'Femenino', 'juana@mail.com', '1123456759', 'Av. Siempre Viva 123', 1)," +
                "('Mario', 'Gómez', 17654321, '1985-09-23', 'Masculino', 'mario@mail.com', '1198765422', 'Calle Falsa 456', 1)," +
                "('Analia', 'Lopez', 23456789, '1992-03-08', 'Femenino', 'analia@mail.com', '1134567840', 'Pasaje 3 de Febrero 789', 1)," +
                "('Patricio', 'Ramirez', 24567890, '1978-12-30', 'Masculino', 'patricio@mail.com', '1145676901', 'Barrio Centro 12', 1)," +
                "('Jimena', 'Fernandez', 35678901, '2000-07-14', 'Femenino', 'jimena@mail.com', '1156729012', 'Zona Norte 334', 1)")

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

    fun registrarPagoActividad(noSocio: Int, actividad: Int, monto: Double): Int {
        val db = this.writableDatabase
        val hoy = java.time.LocalDate.now().toString()

        // Duplicado en el día
        var cursor = db.rawQuery(
            "SELECT 1 FROM pagoActividades WHERE noSocio = ? AND actividad = ? AND fechaPago = ? LIMIT 1",
            arrayOf(noSocio.toString(), actividad.toString(), hoy)
        )
        val yaPagoHoy = cursor.moveToFirst()
        cursor.close()
        if (yaPagoHoy) {
            db.close()
            return -2 // código: ya pagó hoy esa actividad
        }

        // Chequear cupos > 0
        cursor = db.rawQuery(
            "SELECT cupos FROM actividades WHERE nroActividad = ?",
            arrayOf(actividad.toString())
        )
        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return -3 // actividad no existe
        }
        val cuposActuales = cursor.getInt(cursor.getColumnIndexOrThrow("cupos"))
        cursor.close()
        if (cuposActuales <= 0) {
            db.close()
            return -4 // sin cupos
        }

        // insertar pago y decrementar cupo
        var resultado = -1
        db.beginTransaction()
        try {
            val values = android.content.ContentValues().apply {
                put("noSocio", noSocio)
                put("actividad", actividad)
                put("monto", monto)
                put("fechaPago", hoy) // ISO yyyy-MM-dd
            }
            val rowId = db.insert("pagoActividades", null, values)
            if (rowId == -1L) {
                resultado = -1 // error insert
            } else {
                // Decrementar 1 cupo (solo si sigue habiendo > 0)
                val stmt = db.compileStatement(
                    "UPDATE actividades SET cupos = cupos - 1 WHERE nroActividad = ? AND cupos > 0"
                )
                stmt.bindLong(1, actividad.toLong())
                val afectadas = stmt.executeUpdateDelete()

                if (afectadas == 1) {
                    db.setTransactionSuccessful()
                    resultado = rowId.toInt()
                } else {
                    resultado = -5 // no se pudo decrementar cupo
                }
            }
        } finally {
            db.endTransaction()
            db.close()
        }
        return resultado
    }

    fun getPagoActividad(nroPago: Int): Triple<Map<String, Any>, Map<String, Any>, String?>? {
        val db = this.readableDatabase
        val query = """
        SELECT pa.*, ns.*, a.nombre AS nombreActividad
        FROM pagoActividades pa
        INNER JOIN noSocios ns ON ns.nroNoSocio = pa.noSocio
        INNER JOIN actividades a ON a.nroActividad = pa.actividad
        WHERE pa.nroPago = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(nroPago.toString()))
        var pago: MutableMap<String, Any>? = null
        var noSocio: MutableMap<String, Any>? = null
        var nombreActividad: String? = null

        if (cursor.moveToFirst()) {
            // Pago
            pago = mutableMapOf(
                "nroPago" to cursor.getInt(cursor.getColumnIndexOrThrow("nroPago")),
                "noSocio" to cursor.getInt(cursor.getColumnIndexOrThrow("noSocio")),
                "actividad" to cursor.getInt(cursor.getColumnIndexOrThrow("actividad")),
                "monto" to cursor.getDouble(cursor.getColumnIndexOrThrow("monto")),
                "fechaPago" to cursor.getString(cursor.getColumnIndexOrThrow("fechaPago"))
            )

            // No Socio
            noSocio = mutableMapOf(
                "nroNoSocio" to cursor.getInt(cursor.getColumnIndexOrThrow("nroNoSocio")),
                "nombre" to cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                "apellido" to cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                "dni" to cursor.getInt(cursor.getColumnIndexOrThrow("dni")),
                "fechaNac" to cursor.getString(cursor.getColumnIndexOrThrow("fechaNac")),
                "genero" to cursor.getString(cursor.getColumnIndexOrThrow("genero")),
                "mail" to cursor.getString(cursor.getColumnIndexOrThrow("mail")),
                "numCel" to cursor.getString(cursor.getColumnIndexOrThrow("numCel")),
                "domicilio" to cursor.getString(cursor.getColumnIndexOrThrow("domicilio")),
                "aptoFisico" to (cursor.getInt(cursor.getColumnIndexOrThrow("aptoFisico")) == 1)
            )

            nombreActividad = cursor.getString(cursor.getColumnIndexOrThrow("nombreActividad"))
        }

        cursor.close()
        db.close()

        return if (pago != null && noSocio != null) Triple(pago, noSocio, nombreActividad) else null
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
    // Busca un socio por el nro de carnet/socio y devuelve los datos
    fun getSocioPorNro(nroCarnet: Int): Socio? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM socios WHERE nroCarnet = ?",
            arrayOf(nroCarnet.toString())
        )

        var socio: Socio? = null

        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
            val dni = cursor.getInt(cursor.getColumnIndexOrThrow("dni"))
            //Levanta fechas en string
            val fechaNacStr = cursor.getString(cursor.getColumnIndexOrThrow("fechaNac"))
            val fechaIngresoStr = cursor.getString(cursor.getColumnIndexOrThrow("fechaIngreso"))
            val vencCuotaStr = cursor.getString(cursor.getColumnIndexOrThrow("vencCuota"))
            //convierte a LocalDate
            val fechaNac = LocalDate.parse(fechaNacStr)
            val fechaIngreso = LocalDate.parse(fechaIngresoStr)
            val vencCuota = LocalDate.parse(vencCuotaStr)

            val genero = cursor.getString(cursor.getColumnIndexOrThrow("genero"))
            val mail = cursor.getString(cursor.getColumnIndexOrThrow("mail"))
            val numCel = cursor.getString(cursor.getColumnIndexOrThrow("numCel"))
            val domicilio = cursor.getString(cursor.getColumnIndexOrThrow("domicilio"))
            val aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("aptoFisico")) == 1
            val esActivo = cursor.getInt(cursor.getColumnIndexOrThrow("esActivo")) == 1
            val carnetEntregado = cursor.getInt(cursor.getColumnIndexOrThrow("carnetEntregado")) == 1

            socio = Socio(
                nroCarnet,
                nombre,
                apellido,
                dni,
                fechaNac,
                genero,
                mail,
                numCel,
                domicilio,
                aptoFisico,
                fechaIngreso,
                vencCuota,
                esActivo,
                carnetEntregado
            )
        }

        cursor.close()
        db.close()
        return socio
    }
    //ACTUALIZA VENCIMIENTO DE CUOTA DEL SOCIO
    fun updateVencCuota(nroCarnet: Int, nuevaFecha: LocalDate): Int {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("vencCuota", nuevaFecha.toString()) // LocalDate → "YYYY-MM-DD"
        }

        val filasActualizadas = db.update(
            "socios",
            values,
            "nroCarnet = ?",
            arrayOf(nroCarnet.toString())
        )

        db.close()
        return filasActualizadas
    }
    //DEVUELVE LISTA DE SOCIOS CON VENCIMIENTO DE CUOTA EN EL DÍA
    fun getSociosConVencimientoHoy(db: SQLiteDatabase): List<Socio> {
        val socios = mutableListOf<Socio>()
        val hoy = LocalDate.now().toString() // "2025-11-11" por ejemplo

        val query = "SELECT * FROM socios WHERE date(vencCuota) = date(?)"
        val cursor = db.rawQuery(query, arrayOf(hoy))

        if (cursor.moveToFirst()) {
            do {
                val socio = Socio(
                    nroCarnet = cursor.getInt(cursor.getColumnIndexOrThrow("nroCarnet")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                    dni = cursor.getInt(cursor.getColumnIndexOrThrow("dni")),
                    fechaNac = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fechaNac"))),
                    genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")),
                    mail = cursor.getString(cursor.getColumnIndexOrThrow("mail")),
                    numCel = cursor.getString(cursor.getColumnIndexOrThrow("numCel")),
                    domicilio = cursor.getString(cursor.getColumnIndexOrThrow("domicilio")),
                    aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("aptoFisico")) == 1,
                    fechaIngreso = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fechaIngreso"))),
                    vencCuota = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("vencCuota"))),
                    esActivo = cursor.getInt(cursor.getColumnIndexOrThrow("esActivo")) == 1,
                    carnetEntregado = cursor.getInt(cursor.getColumnIndexOrThrow("carnetEntregado")) == 1
                )
                socios.add(socio)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return socios
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

    // CONSULTA DE CUOTA CON SOCIO
    fun getCuotaConSocio(nroCuota: Int): Pair<Cuota, Socio>? {
        val db = this.readableDatabase
        val query = """
        SELECT c.*, s.* 
        FROM cuotas c
        INNER JOIN socios s ON c.nroSocio = s.nroCarnet
        WHERE c.nroCuota = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(nroCuota.toString()))
        var cuota: Cuota? = null
        var socio: Socio? = null

        if (cursor.moveToFirst()) {
            // Cuota
            val nroCuotaDb = cursor.getInt(cursor.getColumnIndexOrThrow("nroCuota"))
            val nroSocio = cursor.getInt(cursor.getColumnIndexOrThrow("nroSocio"))
            val fechaVenc = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fechaVencimiento")))
            val fechaPagoStr = cursor.getString(cursor.getColumnIndexOrThrow("fechaPago"))
            val fechaPago = if (fechaPagoStr != null) LocalDate.parse(fechaPagoStr) else null
            val importe = cursor.getDouble(cursor.getColumnIndexOrThrow("importe"))
            val metodoPago = cursor.getString(cursor.getColumnIndexOrThrow("metodoPago"))
            val cantCuotas = cursor.getInt(cursor.getColumnIndexOrThrow("cantCuotas"))

            cuota = Cuota(nroCuotaDb, nroSocio, fechaVenc, fechaPago, importe, metodoPago, cantCuotas)

            // Socio
            socio = Socio(
                nroCarnet = cursor.getInt(cursor.getColumnIndexOrThrow("nroCarnet")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                dni = cursor.getInt(cursor.getColumnIndexOrThrow("dni")),
                fechaNac = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fechaNac"))),
                genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")),
                mail = cursor.getString(cursor.getColumnIndexOrThrow("mail")),
                numCel = cursor.getString(cursor.getColumnIndexOrThrow("numCel")),
                domicilio = cursor.getString(cursor.getColumnIndexOrThrow("domicilio")),
                aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow("aptoFisico")) == 1,
                fechaIngreso = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("fechaIngreso"))),
                vencCuota = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("vencCuota"))),
                esActivo = cursor.getInt(cursor.getColumnIndexOrThrow("esActivo")) == 1,
                carnetEntregado = cursor.getInt(cursor.getColumnIndexOrThrow("carnetEntregado")) == 1
            )
        }

        cursor.close()
        db.close()

        return if (cuota != null && socio != null) Pair(cuota, socio) else null
    }



}