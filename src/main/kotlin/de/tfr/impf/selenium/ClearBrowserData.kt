package de.tfr.impf.selenium

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

/**
 * Experimental support for resetting the browser to avoid bot detection.
 * Disabled by default.
 * Check out the merge request for discussion and alternatives: https://github.com/TobseF/impf-bot/pull/88
 */
fun clearBrowserData(chromeDriver: ChromeDriver) {

    chromeDriver.get("chrome://settings/clearBrowserData")

    val root1 = chromeDriver.findElement(By.cssSelector("settings-ui"))
    // get 1st shadow root element
    val shadowRoot1 = expandRootElement(chromeDriver, root1)

    // get 2nd shadow root element
    val shadowRoot2 = shadowRoot1.findAndExpandShadowElement("settings-main", chromeDriver)

    // get 3rd shadow root element
    val shadowRoot3 = shadowRoot2.findAndExpandShadowElement("settings-basic-page", chromeDriver)

    // get 4th shadow root element
    val shadowRoot4 = shadowRoot3.findAndExpandShadowElement("settings-section > settings-privacy-page", chromeDriver)

    // get 5th shadow root element
    val shadowRoot5 = shadowRoot4.findAndExpandShadowElement("settings-clear-browsing-data-dialog", chromeDriver)

    // get 6th parent
    val root6 = shadowRoot5.findElement(By.cssSelector("#clearBrowsingDataDialog"))

    // get button (finally!)
    val clearDataButton = root6.findElement(By.cssSelector("#clearBrowsingDataConfirm"))
    // end identify clear data button via nested Shadow Dom elements

    clearDataButton.click(); // click that hard to reach button!

    Thread.sleep(5000)

    chromeDriver.manage().deleteAllCookies()
}

fun WebElement.findAndExpandShadowElement(cssSelector: String, driver: ChromeDriver): WebElement {
    return expandRootElement(driver, this.findElement(By.cssSelector(cssSelector)))
}

private fun expandRootElement(driver: ChromeDriver, element: WebElement): WebElement {
    val executor = driver as JavascriptExecutor
    return executor.executeScript("return arguments[0].shadowRoot", element) as WebElement
}