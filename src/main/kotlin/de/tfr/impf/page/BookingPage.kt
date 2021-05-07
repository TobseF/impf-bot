package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

/**
 * Three step booking page. Displays the vaccination dates.
 */
class BookingPage(driver: WebDriver) : AbstractPage(driver) {

    fun title(): WebElement? = findAnyBy("//h1")

    override fun isDisplayed() = title()?.text == "Onlinebuchung für Ihre Corona-Schutzimpfung"

    fun isDisplayingVaccinationDates(): Boolean {
        return findAll("//div[contains(@class, 'its-search-step-title') and contains(text(), 'Impftermine')]").isNotEmpty()
    }

}