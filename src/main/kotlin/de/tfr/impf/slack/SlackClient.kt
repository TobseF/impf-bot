package de.tfr.impf.slack

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.response.api.ApiTestResponse
import de.tfr.impf.config.Config
import mu.KotlinLogging


fun main() {

}

val log = KotlinLogging.logger("ReportJob")

class SlackClient {

    private val channelName = Config.slackBotChannel
    private val apiToken = Config.slackBotApiToken

    fun sendMessage(message: String) {
        val slack = Slack.getInstance()
        val methods: MethodsClient = slack.methods(apiToken)
        sendToChannel(methods, message)
    }

    private fun sendToChannel(methods: MethodsClient, message: String) {
        println("post message")
        val request = ChatPostMessageRequest.builder()
            .channel(channelName)
            .text(":wave: $message")
            .build()
        val response = methods.chatPostMessage(request)
        println(response)
    }

    /**
     * Only for debugging
     */
    fun testConnection() {
        val slack = Slack.getInstance();
        val response: ApiTestResponse = slack.methods().apiTest { r -> r.foo("bar") };
        log.info { "Response: $response" }
    }

}