package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class LocationPage(driver: WebDriver) : AbstractPage(driver) {

    fun title(): WebElement = findBy("//h1")

    fun validate() = title().text == "Wurde Ihr Anspruch auf eine Corona-Schutzimpfung bereits geprüft?"

    /**
     * Already approved -> No
     */
    fun askForApproval() {
        findBy("//input[@type='radio' and @name='vaccination-approval-checked']//following-sibling::span[contains(text(),'Nein')]/..").click()
        //findBy("//input[@class='ets-radio-control' and and contains(text(), 'Nein')]]")
    }

    fun submitInput() {
        findBy("//button[@type='submit']").click()
    }

    /**
     * Are you an approved person -> yes
     */
    fun checkCorrectPerson() {
        findBy("//input[@type='radio' and @formcontrolname='isValid']//following-sibling::span[contains(text(),'Ja')]/..").click()
    }

    fun enterAge(age: Int) {
        findBy("//input[@formcontrolname='age']").sendKeys("" + age)
    }

    fun acceptCookies() {
        val cookieNags: MutableList<WebElement> = findAll("//a[contains(text(),'Auswahl bestätigen')]")
        cookieNags.firstOrNull()?.click()
    }

    fun isFull(): Boolean {
        return findAll("//div[contains(@class, 'alert-danger') and contains(text(), 'keine')]").isNotEmpty()
    }

    fun isFull2(): Boolean {
        return findAll("//div[contains(@class, 'alert-danger') and contains(text(),'keine')]").isNotEmpty()
    }
}