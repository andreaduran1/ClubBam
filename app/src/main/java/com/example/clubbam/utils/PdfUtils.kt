package com.example.clubbam.utils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap

object PdfUtils {
    fun generarPDFdesdeCard(context: Context, cardView: View, nombreArchivo: String) {
        try {
            // Renderiza la vista como bitmap
            val bitmap = createBitmap(cardView.width, cardView.height)
            val canvas = Canvas(bitmap)
            cardView.draw(canvas)

            // Crea el documento PDF
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = document.startPage(pageInfo)
            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            document.finishPage(page)

            // Guarda el PDF en Descargas
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$nombreArchivo.pdf"
            )
            document.writeTo(FileOutputStream(file))
            document.close()

            Toast.makeText(context, "PDF guardado en Descargas", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
