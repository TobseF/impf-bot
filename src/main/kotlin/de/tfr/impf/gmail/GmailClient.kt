package de.tfr.impf.gmail

import de.tfr.impf.config.Config
import mu.KotlinLogging
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.EmailException
import org.apache.commons.mail.HtmlEmail

val log = KotlinLogging.logger("GmailClient")

class GmailClient {
    private val gmailAppPassword = Config.gmailAppPassword
    private val senderEmail = Config.gmailEmailFrom
    private val toMail = Config.gmailEmailTo
    fun sendMessage(message: String) {
        val email = HtmlEmail()
        email.hostName = "smtp.googlemail.com"
        email.setSmtpPort(465)
        email.isSSLOnConnect = true
        email.subject = "impf-bot"
        try {
            email.setAuthenticator(DefaultAuthenticator(senderEmail, gmailAppPassword))
            email.setFrom(senderEmail)
            email.addTo(toMail)
            email.setMsg(message)
            email.send()
            log.info { "Email has been sent to: $toMail" }
        } catch (ex: EmailException) {
            log.error { "Error sending email: $ex" }
        }
    }
}