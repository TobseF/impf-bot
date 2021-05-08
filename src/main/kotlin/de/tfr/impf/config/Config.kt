package de.tfr.impf.config

import java.util.concurrent.TimeUnit

object Config : KProperties() {

    init {
        loadProperties("config.properties")
    }

    val mainPageUrl: String by lazyProperty()
    private val locations: String by lazyProperty()

    /**
     * Comma separated list of locations (e.g. "69123 Heidelberg,76137 Karlsruhe")
     */
    fun locationList() = locations.split(",").map(this::parseLocation)

    val sendRequest: Boolean by lazyBoolProperty()

    private val waitingTime: Long by lazyLongProperty()

    /**
     * Waiting time before checking the next location in milliseconds [ms]
     */
    fun waitingTime(): Long = TimeUnit.SECONDS.toMillis(waitingTime)

    private val searchElementTimeout: Long by lazyLongProperty()

    /**
     * Timeout when searching an element on the page in milliseconds [ms]
     */
    fun searchElementTimeout(): Long = TimeUnit.SECONDS.toMillis(searchElementTimeout)

    private val waitingTimeForUserAction: Long by lazyLongProperty()

    private val waitingTimeForBrowser: Long by lazyLongProperty()

    private val waitingTimeInWaitingRoom: Long by lazyLongProperty()


    /**
     * Explicit waiting time for browser updates in milliseconds [ms]
     */
    fun waitingTimeForBrowser(): Long = TimeUnit.SECONDS.toMillis(waitingTimeForBrowser)

    /**
     * Waiting time for a manual user interaction in milliseconds [ms]
     */
    fun waitingTimeForUserAction(): Long = TimeUnit.MINUTES.toMillis(waitingTimeForUserAction)

    /**
     * Waiting time in Waiting Room in milliseconds [ms]
     */
    fun waitingTimeInWaitingRoom(): Long = TimeUnit.MINUTES.toMillis(waitingTimeInWaitingRoom)


    val personAge: Int by lazyIntProperty()
    val email: String by lazyProperty()

    /**
     * Mobile number for sms verification. Numbers after the "+49"
     */
    val mobileNumber: String by lazyProperty()

    val nameDriver: String by lazyProperty()
    val exeDriver: String by lazyProperty()
    val pathDriver: String by lazyProperty()

    fun isSlackEnabled() = slackEnabled
    val slackBotApiToken: String by lazyProperty()
    private val slackEnabled: Boolean by lazyBoolProperty()
    val slackBotChannel: String by lazyProperty()
    val slackBotChannelReadSmsName: String by lazyProperty()
    val slackBotChannelReadSmsId: String by lazyProperty()
    val readSmsFromSlack: Boolean by lazyBoolProperty()

    fun isTelegramEnabled() = telegramEnabled
    private val telegramEnabled: Boolean by lazyBoolProperty()
    val telegramApiToken: String by lazyProperty()
    val telegramBotUsername: String by lazyProperty()
    val telegramChatId: String by lazyProperty()

    val userAgent: String by lazyProperty()

    val hasUserAgent = userAgent.isNotEmpty() && userAgent != "default"

    /**
     * @locationStatement location name with optional verification code in square brackets. e.g. "69123 Heidelberg[XXXX-XXXX-XXXX]]"
     */
    private fun parseLocation(locationStatement: String): Location {
        val placementCode = locationStatement.substringAfter("[", "").substringBefore("]", "").ifEmpty { null }
        val name = locationStatement.substringBefore("[")
        return Location(name, placementCode)
    }

    class Location(val name: String, val placementCode: String?) {
        fun hasCode() = placementCode != null

        private fun getCodeSegment(index: Int): String = (placementCode?.split("-")?.get(index)).orEmpty()
        fun getCodeSegment0(): String = getCodeSegment(0)
        fun getCodeSegment1(): String = getCodeSegment(1)
        fun getCodeSegment2(): String = getCodeSegment(2)

        override fun toString(): String {
            return name
        }

    }
}