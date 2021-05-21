package de.tfr.impf.sendgrid

import com.sendgrid.Content
import com.sendgrid.Email
import com.sendgrid.Mail
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGrid
import de.tfr.impf.config.Config
import mu.KotlinLogging
import java.io.IOException


fun main() {
    val sendgridClient = SendgridClient()
    sendgridClient.sendMessage("Test")
}

val log = KotlinLogging.logger("SendgridClient")

class SendgridClient {

    private val apiToken = Config.sendgridApiToken
    private val from = Config.sendgridEmailFrom
    private val to = Config.sendgridEmailTo

    fun sendMessage(message: String) {
        val from = Email(from)
        val subject = "impf-bot"
        val to = Email(to)
        val content = Content("text/plain", message)
        val mail = Mail(from, subject, to, content)
        val sg = SendGrid(apiToken)
        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            sg.api(request)
            log.info { "Email sent successfully." }
        } catch (ex: IOException) {
            log.error { "Error sending email: $ex" }
        }

    }
}
