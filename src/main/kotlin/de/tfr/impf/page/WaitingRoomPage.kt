package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

/**
 * The new cool virtual waiting room
 */
class WaitingRoomPage(driver: WebDriver) : AbstractPage(driver) {

    fun title(): WebElement? = findAnyBy("//h1")

    override fun isDisplayed() = title()?.text == "Virtueller Warteraum des Impfterminservice"

}