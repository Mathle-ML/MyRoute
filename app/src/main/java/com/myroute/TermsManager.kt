package com.myroute

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class TermsManager {
    companion object {
        private const val TERMS_FILE_NAME = "terms.txt"

        fun areTermsAccepted(context: Context): Boolean {
            if(!createTermsFileIfNotExists(context))return false

            try {
                val inputStream = File(context.filesDir, TERMS_FILE_NAME).inputStream()
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()

                val termsText = String(buffer)
                return termsText.trim().toBoolean()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return false
        }

        fun setTermsAccepted(context: Context) {
            if(!createTermsFileIfNotExists(context))return

            try {
                val outputStreamWriter = OutputStreamWriter(context.openFileOutput(TERMS_FILE_NAME, Context.MODE_PRIVATE))
                outputStreamWriter.write("true")
                outputStreamWriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun createTermsFileIfNotExists(context: Context) : Boolean {
            val file = File(context.filesDir, TERMS_FILE_NAME)
            if (!file.exists()) {
                try {
                    file.createNewFile()
                    true
                } catch (e: IOException) {
                    e.printStackTrace()
                    false
                }
            }
            return true
        }
    }
}