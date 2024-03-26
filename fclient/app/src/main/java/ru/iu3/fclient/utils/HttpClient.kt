package ru.iu3.fclient.utils

import android.util.Log
import org.apache.commons.io.IOUtils
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

fun testHttpClient(result: (String) -> Unit) {
    Thread {
        try {
            val httpConnection =
                URL("https://www.wikipedia.org").openConnection() as HttpURLConnection
            val inputStream = httpConnection.inputStream
            val html = IOUtils.toString(inputStream)
            val title = getPageTitle(html)
            result(title)

        } catch (e: Exception) {
            Log.e("TAG", "testHttpClient: ", e)
        }
    }.start()
}

fun getPageTitle(html: String): String {
    val pattern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL)
    val matcher = pattern.matcher(html)
    return if (matcher.find()) matcher.group(1).trim() else "Not found"
}

