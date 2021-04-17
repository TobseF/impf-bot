package de.tfr.impf

import de.tfr.impf.config.Config
import de.tfr.impf.page.CookieNagComponent
import de.tfr.impf.page.LocationPage
import de.tfr.impf.page.MainPage
import de.tfr.impf.selenium.createDriver
import de.tfr.impf.slack.SlackClient
import mu.KotlinLogging
import org.openqa.selenium.WebDriver

val log = KotlinLogging.logger("ReportJob")

class ReportJob {

    private var driver: WebDriver = createDriver()
    private val locations = Config.locationList()
    private val personAge = Config.personAge

    fun reportFreeSlots() {
        log.info { "Person age: $personAge" }
        log.info { "Started checking these ${locations.size} locations:\n$locations" }
        while (true) {
            checkLocations()
        }
    }

    private fun checkLocations() {
        for (location in locations) {
            try {
                checkLocation(location)
            } catch (e: Exception) {
                log.error(e) { "Failed to check location: $location\n" + e.message }
            }
        }
    }


    private fun checkLocation(location: String) {
        val mainPage = openMainPage(driver)
        val cookieNag = CookieNagComponent(driver)
        mainPage.validate()
        mainPage.chooseLocation(location)
        Thread.sleep(500)
        cookieNag.acceptCookies()
        mainPage.submitLocation()
        val locationPage = LocationPage(driver)
        if (locationPage.validate()) {
            log.debug { "Changed to location page: $location" }
            locationPage.askForApproval()
            Thread.sleep(2000)
            cookieNag.acceptCookies()
            if (locationPage.isFull()) {
                log.debug { "Location: $location is full" }
            } else {
                locationPage.checkCorrectPerson()
                locationPage.enterAge(personAge)
                locationPage.submitInput()
                Thread.sleep(2000)
                if (locationPage.isFull()) {
                    log.debug { "Location: $location is full" }
                } else {
                    sendMessage(location)
                    val minutes = 20L
                    Thread.sleep(minutes * 60 * 1000)
                }
            }
        }
    }

    private fun sendMessage(location: String) {
        val message = "Found free seats in location $location:${driver.currentUrl}"
        log.info { message }
        if (Config.isSlackEnabled()) {
            SlackClient().sendMessage(message)
        }
    }

    private fun openMainPage(driver: WebDriver): MainPage {
        val mainPage = MainPage(driver)
        mainPage.open()
        log.debug { "Choose State: " + mainPage.chooseState()?.text }
        mainPage.chooseState()?.click()
        mainPage.chooseStateItemBW()
        return mainPage
    }
}

