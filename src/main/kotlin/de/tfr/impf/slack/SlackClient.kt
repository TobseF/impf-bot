package de.tfr.impf.slack

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.SlackApiException
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.request.conversations.ConversationsHistoryRequest.ConversationsHistoryRequestBuilder
import com.slack.api.methods.request.conversations.ConversationsJoinRequest
import com.slack.api.methods.request.conversations.ConversationsListRequest.ConversationsListRequestBuilder
import com.slack.api.methods.response.api.ApiTestResponse
import de.tfr.impf.config.Config
import mu.KotlinLogging
import java.io.IOException


fun main() {
    val slackClient = SlackClient()

    slackClient.listChannelIds() // only needed once to find the channel id
    //slackClient.joinChannel("CE728N1PY") // only needed once

    //slackClient.testConnection() // not needed - only for testing
}

val log = KotlinLogging.logger("SlackClient")

class SlackClient {

    private val channelName = Config.slackBotChannel
    private val apiToken = Config.slackBotApiToken

    private val slack = Slack.getInstance()
    private val methods: MethodsClient = slack.methods(apiToken)

    private val readSmsChannelId = Config.slackBotChannelReadSmsId
    private val readSmsChannelName = Config.slackBotChannelReadSmsName

    private val messageSsmOK = "sms-ok:"
    private val messageSuffixSMS = "sms:"

    /**
     * List all available channels whith id. Useful to find the channel if for [readSmsChannelId]
     */
    fun listChannelIds() {
        // you can get this instance via ctx.client() in a Bolt app
        val client = Slack.getInstance().methods(apiToken)
        try {
            // Call the conversations.list method using the built-in WebClient
            val result = client.conversationsList { r: ConversationsListRequestBuilder -> r }
            result?.channels?.let {
                for (channel in it) {
                    log.info("Channel: " + channel.name + " id: " + channel.id)
                }
            }
        } catch (e: IOException) {
            log.error("error: " + e.message, e)
        } catch (e: SlackApiException) {
            log.error("error: " + e.message, e)
        }
    }

    fun sendMessage(message: String) {
        sendToChannel(message)
    }

    fun acceptSms(code: String = "") {
        sendToSmsChannel(messageSsmOK + code);
    }

    private fun sendToSmsChannel(message: String) = sendToChannel(message, readSmsChannelName)

    private fun sendToChannel(message: String, channelName: String = this.channelName) {
        log.debug { "Sending Slack message: $message" }
        val request = ChatPostMessageRequest.builder()
            .channel(channelName)
            .text(message)
            .build()
        val response = methods.chatPostMessage(request)
        log.debug { "Slack response: $response" }
    }

    /**
     * Fetch conversation history using the ID
     */
    fun findLasSmsCode(channel: String = readSmsChannelId): String? {
        val lastTenMinutes = (System.currentTimeMillis() - (1000 * 10)).toString()
        val client = Slack.getInstance().methods(apiToken)

        try {

            // Call the chat.postMessage method using the built-in WebClient
            val result = client.conversationsHistory { r: ConversationsHistoryRequestBuilder ->
                r
                    .token(apiToken)
                    .channel(channel) // Channel-id, not the name!
                    .latest(lastTenMinutes)
                    .inclusive(true)
                    .limit(4)
            }
            log.debug { "result: $result" }

            result.messages?.forEach { message ->
                val text = message.text
                if (text.startsWith(messageSsmOK)) {
                    // ignore an already accepted sms
                    return null
                }
                if (text.startsWith(messageSuffixSMS)) {
                    val smsCode = text.substringAfter(messageSuffixSMS)
                    return smsCode.ifEmpty { null }
                }
            }
        } catch (e: IOException) {
            log.error("error: " + e.message, e)
        } catch (e: SlackApiException) {
            log.error("error: " + e.message, e)
        }
        return null
    }

    /**
     * Only needed once to let the bot join to the sms channel
     */
    fun joinChannel(channelID: String) {
        val conversationsJoin =
            methods.conversationsJoin { r: ConversationsJoinRequest.ConversationsJoinRequestBuilder ->
                r.channel(channelID)
            }
        log.info { "Join response; $conversationsJoin" }
    }

    /**
     * Only for debugging
     */
    fun testConnection() {
        val slack = Slack.getInstance()
        val response: ApiTestResponse = slack.methods().apiTest { r -> r.foo("bar") }
        log.info { "Response: $response" }
    }

}