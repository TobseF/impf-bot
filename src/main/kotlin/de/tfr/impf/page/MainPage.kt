package de.tfr.impf.page

import de.tfr.impf.config.Config
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class MainPage(driver: WebDriver) : AbstractPage(driver) {

    fun open() {
        driver.get(Config.mainPageUrl)
    }

    fun chooseState(): WebElement? = findAll("//span[@role='combobox']").firstOrNull()

    fun chooseLocation(): WebElement? = findAll("//span[@role='combobox']").getOrNull(1)

    fun chooseStateItemBW() =
        findAll("//li[@role='option' and contains(text() , 'Baden-Württemberg')]").firstOrNull()?.click()

    fun title(): WebElement = findBy("//h1")

    override fun isDisplayed() = title().text == "Buchen Sie die Termine für Ihre Corona-Schutzimpfung"

    fun chooseLocation(locationName: String) {
        chooseLocation()?.click()
        findAnyBy("//li[@role='option' and contains(text() , '$locationName')]")?.click()
    }

    fun submitLocation() {
        findAnyBy("//button[@type='submit']")?.click()
    }


}


