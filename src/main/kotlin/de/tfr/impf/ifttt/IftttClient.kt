package de.tfr.impf.ifttt

import de.tfr.impf.config.Config
import de.tfr.impf.sendgrid.log
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class IftttClient {
    fun sendMessage(message: String) {
        val apiKey = Config.iftttApiKey
        val eventName = Config.iftttEventName

        val values = mapOf("value1" to message, "value2" to "https://116117.app/")

        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://maker.ifttt.com/trigger/$eventName/with/key/$apiKey"))
            .POST(formData(values))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info { "Notification via IFTTT sent successfully." }
    }
}

fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

fun formData(data: Map<String, String>): HttpRequest.BodyPublisher? {

    val res = data.map {(k, v) -> "${(k.utf8())}=${v.utf8()}"}
        .joinToString("&")

    return HttpRequest.BodyPublishers.ofString(res)
}
