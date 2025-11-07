package com.example.clubbam.model

import java.time.LocalDate

data class Cuota(
    val nroCuota: Int,
    val nroSocio: Int,
    val fechaVencimiento: LocalDate,
    val fechaPago: LocalDate?,
    val importe: Double,
    val metodoPago: String,
    val cantCuotas: Int
)
