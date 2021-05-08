package de.tfr.impf.config

import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.util.*
import kotlin.reflect.KProperty

open class KProperties {

    private val properties = Properties()

    protected fun loadProperties(propertyFile: String) {
        val stream = Config.javaClass.classLoader.getResourceAsStream(propertyFile)
            ?: throw FileNotFoundException("Failed locating resource: $propertyFile")
        // Used InputStreamReader and InputStream to force Properties to load in UTF8
        return properties.load(InputStreamReader(stream, "UTF-8"))
    }

    fun getProperty(propertyKey: String): String? {
        return properties.getProperty(propertyKey)
    }

    fun lazyProperty() = LazyProperty(this) { it }
    fun lazyIntProperty() = LazyProperty(this) { it.toInt() }
    fun lazyLongProperty() = LazyProperty(this) { it.toLong() }
    fun lazyBoolProperty() = LazyProperty(this, defaultValue = false) { it.toBoolean() }

    open class LazyProperty<T>(
        private val props: KProperties,
        private val defaultValue: T? = null,
        private val typeMapper: (String) -> T
    ) {
        private var storedValue: T? = null

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            var value: T? = storedValue
            return if (value == null) {
                val valueFromFile = props.getProperty(property.name)
                if (valueFromFile == null) {
                    if (defaultValue == null) {
                        throw IllegalStateException("Failed finding property `${property.name}`")
                    }
                    defaultValue
                } else {
                    val convertedValue = typeMapper.invoke(valueFromFile)
                    storedValue = convertedValue
                    convertedValue
                }
            } else {
                value
            }
        }
    }

}