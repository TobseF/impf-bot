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
    fun locationList() = locations.split(",").map(this::parseLocation)

    val sendRequest: Boolean by lazyBoolProperty()
    val personAge: Int by lazyIntProperty()
    val email: String by lazyProperty()

    /**
     * Mobile number for sms verification. Numbers after the "+49"
     */
    val mobileNumber: String by lazyProperty()

    val nameDriver: String by lazyProperty()
    val exeDriver: String by lazyProperty()
    val pathDriver: String by lazyProperty()
    val timeOutDefault = 1600L
    val timeOutLong = 6500L

    fun isSlackEnabled() = slackEnabled
    val slackBotApiToken: String by lazyProperty()
    private val slackEnabled: Boolean by lazyBoolProperty()
    val slackBotChannel: String by lazyProperty()
    val slackBotChannelReadSmsName: String by lazyProperty()
    val slackBotChannelReadSmsId: String by lazyProperty()

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