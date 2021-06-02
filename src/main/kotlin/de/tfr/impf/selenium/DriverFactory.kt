package de.tfr.impf.selenium

import de.tfr.impf.config.Config
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.util.concurrent.TimeUnit
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement

fun createDriver(): ChromeDriver {
    WebDriverManager.chromedriver().setup();
    val chromeOptions = ChromeOptions()
    if (Config.hasUserAgent) {
        chromeOptions.addArguments("user-agent=" + Config.userAgent)
    }

    val chromeDriver = ChromeDriver(chromeOptions)
    chromeDriver.setTimeOut(Config.searchElementTimeout())

    chromeDriver.get("chrome://settings/clearBrowserData")

    val root1 = chromeDriver.findElement(By.cssSelector("settings-ui"))
    // get 1st shadowroot element
    val shadowRoot1 = expandRootElement(chromeDriver, root1)

    // get 2nd parent
    val root2 = shadowRoot1.findElement(By.cssSelector("settings-main"))
    // get 2nd shadowroot element
    val shadowRoot2 = expandRootElement(chromeDriver, root2)

    // get 3rd parent
    val root3 = shadowRoot2.findElement(By.cssSelector("settings-basic-page"))
    // get 3rd shadowroot element
    val shadowRoot3 = expandRootElement(chromeDriver, root3)

    // get 4th parent
    val root4 = shadowRoot3.findElement(By.cssSelector("settings-section > settings-privacy-page"))
    // get 4th shadowroot element
    val shadowRoot4 = expandRootElement(chromeDriver, root4)

    // get 5th parent
    val root5 = shadowRoot4.findElement(By.cssSelector("settings-clear-browsing-data-dialog"))
    // get 5th shadowroot element
    val shadowRoot5 = expandRootElement(chromeDriver, root5)

    // get 6th parent
    val root6 = shadowRoot5.findElement(By.cssSelector("#clearBrowsingDataDialog"))

    // get button (finally!)
    val clearDataButton = root6.findElement(By.cssSelector("#clearBrowsingDataConfirm"))
    // end identify clear data button via nested Shadow Dom elements

    clearDataButton.click(); // click that hard to reach button!

    Thread.sleep(5000)

    chromeDriver.manage().deleteAllCookies()

    return chromeDriver
}

private fun expandRootElement(driver: ChromeDriver, element: WebElement): WebElement {
    val executor = driver as JavascriptExecutor
    return executor.executeScript("return arguments[0].shadowRoot", element) as WebElement
}

fun WebDriver.setTimeOut(milliseconds: Long) {
    this.manage()?.timeouts()?.implicitlyWait(milliseconds, TimeUnit.MILLISECONDS)
}
