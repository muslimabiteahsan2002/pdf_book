@file:Suppress("DEPRECATION")

package com.muslima.mypdf

import android.os.AsyncTask
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

@Suppress("DEPRECATION")
class MyOnlinePdf(private val pdfUrl: String,private val l: Online): AsyncTask<String, Void, InputStream?>() {
    interface Online{
        fun online(result: InputStream?)
    }
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg p0: String?): InputStream? {
        try {
            val url = URL(pdfUrl)
            val connection = url.openConnection() as HttpURLConnection
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                return BufferedInputStream(connection.inputStream)
            }
        } catch (e: MalformedURLException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: InputStream?) {
        super.onPostExecute(result)
        l.online(result)
    }
}