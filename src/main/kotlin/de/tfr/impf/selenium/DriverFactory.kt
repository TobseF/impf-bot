package de.tfr.impf.selenium

import de.tfr.impf.config.Config
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.util.concurrent.TimeUnit

fun createDriver(): ChromeDriver {
    WebDriverManager.chromedriver().setup();
    val chromeOptions = ChromeOptions()
    if (Config.hasUserAgent) {
        chromeOptions.addArguments("user-agent=" + Config.userAgent)
    }

    val chromeDriver = ChromeDriver(chromeOptions)
    chromeDriver.setTimeOut(Config.searchElementTimeout())

    if (Config.clearCookies) {
        chromeDriver.manage().deleteAllCookies()
    }

    if (Config.experimentalClearBrowser) {
        clearBrowserData(chromeDriver)
    }

    return chromeDriver
}


fun WebDriver.setTimeOut(milliseconds: Long) {
    this.manage()?.timeouts()?.implicitlyWait(milliseconds, TimeUnit.MILLISECONDS)
}
