package de.tfr.impf.test.util

import java.net.URL
import kotlin.reflect.KClass




fun getResourceAsString(clazz: Any, fileName: String) = getResource(clazz::class, fileName).toString()

fun getResource(clazz: KClass<out Any>, fileName: String): URL {
    val papageName = clazz.java.`package`.name
    val filePath = papageName.replace(".","/") + "/" + fileName
    try {
        return clazz.java.classLoader.getResource(filePath)!!

    } catch (e: IllegalStateException) {
        throw IllegalStateException("Failed finding testfile: $filePath")
    }
}
