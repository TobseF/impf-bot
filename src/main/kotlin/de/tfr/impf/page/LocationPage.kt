package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class LocationPage(driver: WebDriver) : AbstractPage(driver) {

    fun title(): WebElement? = findAnyBy("//h1")

    override fun isDisplayed() = title()?.text == "Wurde Ihr Anspruch auf eine Corona-Schutzimpfung bereits geprüft?"

    /**
     * Already approved -> No
     */
    fun askForApproval() {
        findBy("//input[@type='radio' and @name='vaccination-approval-checked']//following-sibling::span[contains(text(),'Nein')]/..").click()
    }

    fun submitInput() {
        findBy("//button[@type='submit']").click()
    }

    /**
     * Are you an approved person -> yes
     */
    fun checkCorrectPerson() {
        findAnyBy("//input[@type='radio' and @formcontrolname='isValid']//following-sibling::span[contains(text(),'Ja')]/..")?.click()
    }

    fun enterAge(age: Int) {
        findBy("//input[@formcontrolname='age']").sendKeys("" + age)
    }

    /**
     * Locates the warning on top or on bottom
     * @return free seats are not available
     */
    fun isFull(): Boolean {
        return findAll("//div[contains(@class, 'alert-danger') and contains(text(), 'keine')]").isNotEmpty()
    }

}