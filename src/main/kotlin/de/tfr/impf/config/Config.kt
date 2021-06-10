package de.tfr.impf.config

import java.util.concurrent.TimeUnit

object Config : KProperties() {

    init {
        loadProperties("config.properties")
    }

    val mainPageUrl: String by lazyProperty()
    val state: String by lazyProperty()
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


    /**
     *  Birth date verification field [DD.MM.YYYY].
     */
    val birthDate: String by lazyProperty()
    val email: String by lazyProperty()

    /**
     * Mobile number for sms verification. Numbers after the "+49"
     */
    val mobileNumber: String by lazyProperty()

    /**
     * Automatically selects the first possible vaccination date and enters all required personal data based on the following values to book this vaccination slot for you.
     */
    val bookingEnabled: Boolean by lazyBoolProperty()

    /**
     * Takes screenshots during booking process to have proof of you booking.
     */
    val takeScreenshots: Boolean by lazyBoolProperty()

    /**
     * Defines the path were all screenshots will be stored in (Optional)
     */
    val outputPath: String by lazyProperty()
    /**
     * Salutation (m=man, w=women, d=divers, c=child)
     */
    val personalDataSalutation: String by lazyProperty()

    /**
     * Firstname
     */
    val personalDataFirstname: String by lazyProperty()

    /**
     * Lastname
     */
    val personalDataLastname: String by lazyProperty()

    /**
     * Zipcode
     */
    val personalDataZipcode: Int by lazyIntProperty()

    /**
     * City
     */
    val personalDataCity: String by lazyProperty()

    /**
     * Street
     */
    val personalDataStreet: String by lazyProperty()

    /**
     * House number
     */
    val personalDataHouseNumber: String by lazyProperty()

    /**
     * Phone number. Numbers after the "+49"
     */
    val personalDataMobileNumber: String by lazyProperty()

    /**
     * E-Mail
     */
    val personalDataEmail: String by lazyProperty()

    /**
     * Clears browser cookies to avoid bot detection. Disabled as default.
     */
    val clearCookies = false

    /**
     * Experimental feature, which tries to clear the browser cache to avoid bot detection. Disabled as default.
     */
    val experimentalClearBrowser: Boolean by lazyBoolProperty()

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

    fun isSendgridEnabled() = sendgridEnabled
    private val sendgridEnabled: Boolean by lazyBoolProperty()
    val sendgridApiToken: String by lazyProperty()
    val sendgridEmailFrom: String by lazyProperty()
    val sendgridEmailTo: String by lazyProperty()

    fun isGmailEnabled() = gmailEnabled
    private val gmailEnabled: Boolean by lazyBoolProperty()
    val gmailAppPassword: String by lazyProperty()
    val gmailEmailFrom: String by lazyProperty()
    val gmailEmailTo: String by lazyProperty()

    fun isIFTTTEnabled() = iftttEnabled
    private val iftttEnabled: Boolean by lazyBoolProperty()
    val iftttApiKey: String by lazyProperty()
    val iftttEventName: String by lazyProperty()

    fun isAlarmEnabled() = alarmEnabled
    private val alarmEnabled: Boolean by lazyBoolProperty()
    val alarmDuration: Int by lazyIntProperty()
    val alarmFrequency: Int by lazyIntProperty()
    val alarmVolumeInPercentage: Double by lazyDoubleProperty()

    val userAgent: String by lazyProperty()

    val hasUserAgent = userAgent.isNotEmpty() && userAgent != "default"

    /**
     * @locationStatement location name with optional verification code in square brackets. e.g. "69123 Heidelberg[XXXX-XXXX-XXXX]]"
     */
    private fun parseLocation(locationStatement: String): Location {
        val placementCode = locationStatement.substringAfter("[", "").substringBefore("]", "").ifEmpty { null }
        val serverCode = locationStatement.substringAfter("(","").substringBefore(")", "").ifEmpty { null }
        val name = locationStatement.substringBefore("[")
        return Location(name, placementCode, serverCode)
    }

    class Location(val name: String, val placementCode: String?, val serverCode: String?) {
        fun hasCode() = placementCode != null
        fun hasServerCode() = serverCode != null

        private fun getCodeSegment(index: Int): String = (placementCode?.split("-")?.get(index)).orEmpty()
        fun getCodeSegment0(): String = getCodeSegment(0)
        fun getCodeSegment1(): String = getCodeSegment(1)
        fun getCodeSegment2(): String = getCodeSegment(2)

        override fun toString(): String {
            return name
        }

    }
}
