package de.tfr.impf.telegram

import com.elbekD.bot.Bot
import de.tfr.impf.config.Config

class TelegramClient{
    fun sendMessage(message: String) {
        val token = Config.telegramApiToken
        val username = Config.telegramBotUsername
        val chatId = Config.telegramChatId
        val bot = Bot.createPolling(username, token)
        bot.start()
        bot.sendMessage(chatId = chatId, text = message)
    }
}