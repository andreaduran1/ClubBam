package com.example.clubbam.model

import java.time.LocalDate

class Socio(
    val nroCarnet: Int,
    var nombre: String,
    var apellido: String,
    var dni: Int,
    var fechaNac: LocalDate,
    var genero: String,
    var mail: String,
    var numCel: String,
    var domicilio: String,
    var aptoFisico: Boolean,
    var fechaIngreso: LocalDate,
    var vencCuota: LocalDate,
    var esActivo: Boolean,
    var carnetEntregado: Boolean
)
