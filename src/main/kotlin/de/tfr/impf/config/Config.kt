package de.tfr.impf.config

object Config : KProperties() {

    init {
        loadProperties("config.properties")
    }

    val mainPageUrl: String by lazyProperty()
    private val locations: String by lazyProperty()
    fun locationList() = locations.split(",")
    val personAge: Int by lazyIntProperty()

    val nameDriver: String by lazyProperty()
    val exeDriver: String by lazyProperty()
    val pathDriver: String by lazyProperty()
    val timeOutDefault = 1600L
    val timeOutLong = 6500L

    fun isSlackEnabled() = slackEnabled.isTrue()
    val slackBotApiToken: String by lazyProperty()
    private val slackEnabled: String by lazyProperty()
    val slackBotChannel: String by lazyProperty()

}