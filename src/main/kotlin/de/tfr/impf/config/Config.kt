package de.tfr.impf.config

import java.io.FileInputStream
import java.util.*
import kotlin.reflect.KProperty

object Config {

    private var properties: Properties = loadProperties()

    private fun loadProperties(): Properties {
        return Properties().also { it.load(FileInputStream("config.properties")) }
    }

    fun getProperty(propertyKey: String): String {
        return properties.getProperty(propertyKey)
            ?: throw IllegalStateException("Failed finding property `$propertyKey`")
    }

    val mainPageUrl: String by LazyProperty()

    val nameDriver: String by LazyProperty()
    val exeDriver: String by LazyProperty()
    val pathDriver: String by LazyProperty()
    val timeOutDefault = 1600L
    val timeOutLong = 6500L

    val slackBotApiToken: String by LazyProperty()
    private val locations: String by LazyProperty()
    fun locationList() = locations.split(",")
    val slackBotChannel: String by LazyProperty()
    private val slackEnabled: String by LazyProperty()
    fun isSlackEnabled() = slackEnabled == "true"

    private open class LazyProperty {
        private var value = ""
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            if (value.isEmpty()) {
                value = getProperty(property.name)
            }
            return value
        }
    }

}