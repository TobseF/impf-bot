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

    fun getProperty(propertyKey: String): String {
        return properties.getProperty(propertyKey)
            ?: throw IllegalStateException("Failed finding property `$propertyKey`")
    }

    fun lazyProperty() = LazyProperty(this) { it }
    fun lazyIntProperty() = LazyProperty(this) { it.toInt() }
    fun lazyBoolProperty() = LazyProperty(this) { it.toBoolean() }

    open class LazyProperty<T>(private val props: KProperties, private val typeMapper: (String) -> T) {
        private var value = ""
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
            if (value.isEmpty()) {
                value = props.getProperty(property.name)
            }
            return typeMapper.invoke(value)
        }
    }

}