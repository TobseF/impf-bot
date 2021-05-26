package de.tfr.impf.ifttt

import de.tfr.impf.config.Config
import mu.KotlinLogging
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

val log = KotlinLogging.logger("IftttClient")

class IftttClient {
    fun sendMessage(message: String) {
        val apiKey = Config.iftttApiKey
        val eventName = Config.iftttEventName

        val values = mapOf("value1" to message)

        try {
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://maker.ifttt.com/trigger/$eventName/with/key/$apiKey"))
                .POST(formData(values))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build()
            client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (ex: Exception) {
            log.error { "Error sending IFTTT notification: $ex" }
        }

        log.info { "Notification via IFTTT sent successfully." }
    }
}

fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

fun formData(data: Map<String, String>): HttpRequest.BodyPublisher? {

    val res = data.map {(k, v) -> "${(k.utf8())}=${v.utf8()}"}
        .joinToString("&")

    return HttpRequest.BodyPublishers.ofString(res)
}
