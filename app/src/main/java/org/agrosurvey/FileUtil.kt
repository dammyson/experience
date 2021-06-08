package org.agrosurvey

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.*
import java.net.URI
import java.io.FileInputStream as FileInputStream


object FileUtil {

    fun createDirectoryIfNotExist(directory: File){
        if (!directory.exists()) {
            directory.mkdir()
        }
    }
    fun createDirectoryAndSaveFile(imageToSave: Bitmap, directory: File,
                                   fileName: String,
                                   compressFormat : Bitmap.CompressFormat? = null ) : String?{

        if (!directory.exists()) {
            directory.mkdir()
        }
        val file = File(directory, fileName)
        if (file.exists()) {
            file.delete()
        }
        return try {
            val out = FileOutputStream(file)
            if(compressFormat != null)
                imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            file.absolutePath
        } catch (e: Exception) {
            null
        }

    }

    fun savefile(directory: File, fileName: String, sourceFilename: String) {
        val destinationFilename = directory.absolutePath + "/" + fileName

        Log.e("FILE", destinationFilename)
        Log.e("URI", sourceFilename)

        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            bis = BufferedInputStream(FileInputStream(sourceFilename))
            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
        } catch (e: IOException) {
            Log.e("DDDDDDXD ", e.stackTraceToString())
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                Log.e("DDDDD ", e.stackTraceToString())
            }
        }
    }


}
