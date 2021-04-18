package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class SmsVerificationPage(driver: WebDriver) : AbstractPage(driver) {

    fun title(): WebElement? = findAnyBy("//h1")

    override fun isDisplayed() = title()?.text == "SMS Verifizierung"

    fun enterSmsVerificationCode(verificationCode: String) {
        findBy("//input[@formcontrolname='pin']").sendKeys(verificationCode)
    }

    fun submitVerificationCodeLocation() {
        findAnyBy("//button[@type='submit']")?.click()
    }


}