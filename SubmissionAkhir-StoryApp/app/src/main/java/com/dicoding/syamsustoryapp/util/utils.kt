package com.dicoding.syamsustoryapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val JPEG_QUALITY_THRESHOLD = 1000000
private const val JPEG_COMPRESS_QUALITY_START = 100
private const val JPEG_QUALITY_DECREMENT = 5
private const val BUFFER_SIZE = 1024

private val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val myFile = createTempFile(context)

    contentResolver.openInputStream(selectedImg)?.use { inputStream ->
        FileOutputStream(myFile).use { outputStream ->
            val buffer = ByteArray(BUFFER_SIZE)
            var len: Int
            while (inputStream.read(buffer).also { len = it } > 0) {
                outputStream.write(buffer, 0, len)
            }
        }
    }

    return myFile
}

fun reduceImageSize(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = JPEG_COMPRESS_QUALITY_START
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= JPEG_QUALITY_DECREMENT
    } while (streamLength > JPEG_QUALITY_THRESHOLD && compressQuality > 0)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    bitmap.recycle()
    return file
}
