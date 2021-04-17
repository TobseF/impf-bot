package de.tfr.impf.config

object Config : KProperties() {

    init {
        loadProperties("config.properties")
    }

    val mainPageUrl: String by lazyProperty()
    private val locations: String by lazyProperty()

    /**
     * Comma separated list of locations (e.g. "69123 Heidelberg,76137 Karlsruhe")
     */
    fun locationList() = locations.split(",")

    val sendRequest : Boolean by lazyBoolProperty()
    val personAge: Int by lazyIntProperty()
    val email : String by lazyProperty()
    /**
     * Mobile number for sms verification. Numbers after the "+49"
     */
    val mobileNumber : String by lazyProperty()

    val nameDriver: String by lazyProperty()
    val exeDriver: String by lazyProperty()
    val pathDriver: String by lazyProperty()
    val timeOutDefault = 1600L
    val timeOutLong = 6500L

    fun isSlackEnabled() = slackEnabled
    val slackBotApiToken: String by lazyProperty()
    private val slackEnabled: Boolean by lazyBoolProperty()
    val slackBotChannel: String by lazyProperty()

}