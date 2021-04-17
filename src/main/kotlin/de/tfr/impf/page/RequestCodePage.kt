package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class RequestCodePage(driver: WebDriver) : AbstractPage(driver){

    fun title(): WebElement? = findAnyBy("//h1")

    override fun isDisplayed() = title()?.text == "Vermittlungscode anfordern"

    fun emailField() :WebElement? = findAnyBy("//input[@formcontrolname='email']")
    fun mobileNumberField() :WebElement? = findAnyBy("//input[@formcontrolname='phone']")

    fun fillEmail(email: String) = emailField()?.sendKeys(email)

    /**
     * @param mobileNumber mobile number for sms verification. Numbers after the "+49"
     */
    fun fillMobileNumber(mobileNumber: String) = mobileNumberField()?.sendKeys(mobileNumber)

    fun requestCode() {
        findAnyBy("//button[@type='submit']")?.click()
    }

}