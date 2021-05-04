package de.tfr.impf

import de.tfr.impf.config.Config
import de.tfr.impf.page.*
import de.tfr.impf.selenium.createDriver
import de.tfr.impf.slack.SlackClient
import mu.KotlinLogging
import org.openqa.selenium.WebDriver

val log = KotlinLogging.logger("ReportJob")

class ReportJob {

    private var driver: WebDriver = createDriver()

    private val locations = Config.locationList()
    private val personAge = Config.personAge
    private val sendRequest = Config.sendRequest
    private val mobileNumber = Config.mobileNumber
    private val email = Config.email

    private val readSmsFromSlack = true

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
            Thread.sleep(Config.pauseFactor.toLong() * 30 * 1000)
        }
    }


    private fun checkLocation(location: Config.Location) {
        val mainPage = openMainPage(driver)
        val cookieNag = CookieNagComponent(driver)
        mainPage.isDisplayed()
        mainPage.chooseLocation(location.name)
        Thread.sleep(Config.pauseFactor.toLong() * 500)
        cookieNag.acceptCookies()
        mainPage.submitLocation()
        val locationPage = LocationPage(driver)
        if (locationPage.isDisplayed()) {
            log.debug { "Changed to location page: $location" }
            if (location.hasCode()) {
                searchFreeDateByCode(locationPage, location)
            } else {
                checkClaim(locationPage, cookieNag, location)
            }
        }
    }

    private fun searchFreeDateByCode(
        locationPage: LocationPage,
        location: Config.Location
    ) {
        locationPage.confirmClaim()
        val code = location.placementCode
        if (code != null) {
            locationPage.enterCodeSegment0(code)
            locationPage.searchForFreeDate()
            locationPage.searchForVaccinateDate()
            if (locationPage.hasNoVaccinateDateAvailable() || locationPage.hasVacError()) {
                log.debug { "Correct code, but not free vaccination slots: $location" }
            } else {
                sendMessageFoundDates(location)
                waitLongForUserInput()
            }
        }
    }

    private fun checkClaim(
        locationPage: LocationPage,
        cookieNag: CookieNagComponent,
        location: Config.Location
    ) {
        locationPage.askForClaim()
        Thread.sleep(Config.pauseFactor.toLong() * 2000)
        cookieNag.acceptCookies()
        if (locationPage.isFull()) {
            log.debug { "Location: $location is full" }
        } else {
            locationPage.checkCorrectPerson()
            locationPage.enterAge(personAge)
            locationPage.submitInput()
            Thread.sleep(Config.pauseFactor.toLong() * 2000)
            if (locationPage.isFull()) {
                log.debug { "Location: $location is full" }
            } else {
                if (sendRequest) {
                    requestCode(location)
                } else {
                    sendMessageFoundFreeSeats(location)
                    waitLongForUserInput()
                }
            }
        }
    }

    private fun waitLongForUserInput() {
        val minutes = 20L
        Thread.sleep(minutes * 60 * 1000)
    }

    private fun requestCode(location: Config.Location) {
        val requestCodePage = RequestCodePage(driver)
        requestCodePage.fillEmail(email)
        requestCodePage.fillMobileNumber(mobileNumber)
        requestCodePage.requestCode()
        if (requestCodePage.isLimitReached()) {
            log.debug { "Reached request limit in $location" }
        } else {
            sendMessageFoundFreeSeats(location)
            if (readSmsFromSlack) {
                acceptSmsBySlackMessage(location)
            } else {
                waitLongForUserInput()
            }
        }
    }

    /**
     * Reads the sms from a slack channel
     */
    private fun acceptSmsBySlackMessage(location: Config.Location) {
        val slackClient = SlackClient()
        val minutes = 10
        val retries = minutes * 6
        for (i in 0 .. retries) {
            val smsCode = slackClient.findLasSmsCode()
            if (smsCode != null) {
                val smsVerificationPage = SmsVerificationPage(driver)
                smsVerificationPage.enterSmsVerificationCode(smsCode)
                smsVerificationPage.submitVerificationCodeLocation()
                slackClient.acceptSms(smsCode)
            }
            Thread.sleep(10 * 1000)
        }
    }


    private fun sendMessageFoundFreeSeats(location: Config.Location) {
        val message = "Found free seats in location ${location.name}:${driver.currentUrl}" +
                "Five minutes left to send the sms verification"
        sendMessage(message)
    }

    private fun sendMessageFoundDates(location: Config.Location) {
        val message = "Found free vaccination dates in location ${location.name}:${driver.currentUrl}\n" +
                "Ten minutes left to choose a date."
        sendMessage(message)
    }

    private fun sendMessage(message: String) {
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

