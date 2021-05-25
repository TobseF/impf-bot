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
        return findAll("//*[contains(text(), '1. Impftermin')]").isNotEmpty()
    }

    fun selectFirstVaccinationDatePair() =
        findAnyBy("//input[@type='radio' and @formcontrolname='slotPair']//following-sibling::div[contains(@class,'its-slot-pair-search-slot-wrapper')]/..")?.click()

    fun submitInputWithText(text: String) = findAnyBy("//button[@type='submit' and contains(text(), '$text')]")?.click()

    fun isDisplayingSecondBookingStep() = findAnyBy("//button[contains(text(), 'Daten erfassen')]")?.isDisplayed ?: false

    fun selectEnterPersonalData() = findAnyBy("//button[contains(text(),'Daten erfassen')]")?.click()

    fun isDisplayingPersonalDataForm() = findAnyBy("//h2[contains(text(), 'Persönliche Daten')]")?.isDisplayed ?: false

    fun isDisplayingFinalBookingStep() = findAnyBy("//button[contains(text(),'VERBINDLICH BUCHEN')]")?.isDisplayed ?: false

    fun submitFinalBooking() = findAnyBy("//button[contains(text(),'VERBINDLICH BUCHEN')]")?.click()

    private fun selectSalutation(text: String) =
        "//input[@type='radio' and @name='salutation']//following-sibling::span[contains(text(),'$text')]/.."

    fun enterSalutation(salutation: String) {
        when (salutation) {
            "m" -> findAnyBy(selectSalutation("Herr"))?.click()
            "w" -> findAnyBy(selectSalutation("Frau"))?.click()
            "d" -> findAnyBy(selectSalutation("Divers"))?.click()
            "c" -> findAnyBy(selectSalutation("Kind"))?.click()
        }
    }
    fun enterFirstname(firstname: String) {
        findBy("//input[@formcontrolname='firstname']").sendKeys("" + firstname)
    }

    fun enterLastname(lastname: String) {
        findBy("//input[@formcontrolname='lastname']").sendKeys("" + lastname)
    }

    fun enterZipCode(zipCode: Int) {
        findBy("//input[@formcontrolname='zip']").sendKeys("" + zipCode)
    }

    fun enterCity(city: String) {
        findBy("//input[@formcontrolname='city']").sendKeys("" + city)
    }

    fun enterStreet(street: String) {
        findBy("//input[@formcontrolname='street']").sendKeys("" + street)
    }

    fun enterHousenumber(housenumber: String) {
        findBy("//input[@formcontrolname='housenumber']").sendKeys("" + housenumber)
    }

    fun enterPhone(phone: String) {
        findBy("//input[@formcontrolname='phone']").sendKeys("" + phone)
    }

    fun enterEmail(email: String) {
        findBy("//input[@formcontrolname='notificationReceiver']").sendKeys("" + email)
    }
}
