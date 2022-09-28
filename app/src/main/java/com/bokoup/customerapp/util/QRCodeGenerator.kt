package com.bokoup.customerapp.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object QRCodeGenerator {
    private const val QR_CODE_SIZE = 768

    suspend fun generateQR(
        content: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Bitmap =
        withContext(dispatcher) {
            val hints = hashMapOf<EncodeHintType, String>().also {
                it[EncodeHintType.MARGIN] = "1"
                it[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H.toString()
            } // Make the QR code buffer border narrower
            val bits = QRCodeWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                QR_CODE_SIZE,
                QR_CODE_SIZE,
                hints
            )
            Bitmap.createBitmap(QR_CODE_SIZE, QR_CODE_SIZE, Bitmap.Config.RGB_565).also {
                for (x in 0 until QR_CODE_SIZE) {
                    for (y in 0 until QR_CODE_SIZE) {
                        it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
            }
        }
}
