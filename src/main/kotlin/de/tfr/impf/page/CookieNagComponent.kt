package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class CookieNagComponent(driver: WebDriver) : AbstractPage(driver) {

    fun acceptCookies() {
        val cookieNags: MutableList<WebElement> = findAll("//a[contains(text(),'Auswahl bestätigen')]")
        cookieNags.firstOrNull()?.click()
    }

}