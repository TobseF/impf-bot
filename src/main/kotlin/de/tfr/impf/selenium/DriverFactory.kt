package de.tfr.impf.selenium

import de.tfr.impf.config.Config
import mu.KotlinLogging
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.TimeUnit

fun createDriver(): ChromeDriver {
    System.setProperty(Config.nameDriver, Config.pathDriver + Config.exeDriver)
    val chromeDriver = ChromeDriver()
    chromeDriver.setTimeOut(Config.searchElementTimeout())
    return chromeDriver
}

fun WebDriver.setTimeOut(milliseconds: Long) {
    this.manage()?.timeouts()?.implicitlyWait(milliseconds, TimeUnit.MILLISECONDS)
}
