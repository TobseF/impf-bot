package de.tfr.impf

import de.tfr.impf.alarm.Alarm
import de.tfr.impf.config.Config
import de.tfr.impf.gmail.GmailClient
import de.tfr.impf.ifttt.IftttClient
import de.tfr.impf.page.*
import de.tfr.impf.selenium.createDriver
import de.tfr.impf.sendgrid.SendgridClient
import de.tfr.impf.slack.SlackClient
import de.tfr.impf.telegram.TelegramClient
import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import java.io.File
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit

val log = KotlinLogging.logger("ReportJob")

class ReportJob {

    private var driver: WebDriver = createDriver()
    private var startTime = System.currentTimeMillis()

    private val locations = Config.locationList()
    private val birthDate = Config.birthDate
    private val sendRequest = Config.sendRequest
    private val mobileNumber = Config.mobileNumber
    private val email = Config.email
    private val bookingEnabled = Config.bookingEnabled
    private val takeScreenshots = Config.takeScreenshots
    private val outputPath = Config.outputPath
    private val personalDataSalutation = Config.personalDataSalutation
    private val personalDataFirstname = Config.personalDataFirstname
    private val personalDataLastname = Config.personalDataLastname
    private val personalDataZipcode = Config.personalDataZipcode
    private val personalDataCity = Config.personalDataCity
    private val personalDataStreet = Config.personalDataStreet
    private val personalDataHouseNumber = Config.personalDataHouseNumber
    private val personalDataMobileNumber = Config.personalDataMobileNumber
    private val personalDataEmail = Config.personalDataEmail

    fun reportFreeSlots() {
        log.info { "Person age: $birthDate" }
        log.info { "Send requests: $sendRequest" }
        log.info { "mobileNumber: $mobileNumber" }
        log.info { "email: $email" }
        val message = "Starting search for free slots with the following data:\n" +
                "Person age: $birthDate \n" +
                "Send requests: $sendRequest \n" +
                "mobileNumber: $mobileNumber \n" +
                "email: $email"
        sendMessage(message)
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

            if (System.currentTimeMillis() > startTime + (1000 * 60 * 60)) {
                log.info { "Restarting session" }
                driver.quit()
                driver = createDriver()
                startTime = System.currentTimeMillis()
            }

            Thread.sleep(Config.waitingTime())
        }
    }


    private fun checkLocation(location: Config.Location) {
        val cookieNag = CookieNagComponent(driver)
        val mainPage = openMainPage(driver, cookieNag)
        mainPage.isDisplayed()
        cookieNag.acceptCookies()
        mainPage.chooseLocation(location.name)
        Thread.sleep(Config.waitingTimeForBrowser())
        mainPage.submitLocation()

        takeASeatInWaitingRoom()
        cookieNag.acceptCookies()

        val locationPage = LocationPage(driver)
        if (locationPage.isDisplayed()) {
            log.debug { "Changed to location page: $location" }
            if (location.hasCode()) {
                if (location.hasServerCode() && !location.serverCode.isNullOrEmpty()) {
                    locationPage.switchToDifferentServer(location.serverCode)
                }
                searchFreeDateByCode(locationPage, location)
            } else {
                checkClaim(locationPage, cookieNag, location)
            }
        }
    }

    private fun takeASeatInWaitingRoom() {
        val waitingRoom = WaitingRoomPage(driver)
        val waitingTimeEnd = currentTimeMillis() + Config.waitingTimeInWaitingRoom()
        while (waitingRoom.isDisplayed() && currentTimeMillis() < waitingTimeEnd) {
            log.debug { "Waiting in WaitingRoomPage ..." }
            Thread.sleep(Config.waitingTimeForBrowser())
        }
    }

    private fun searchFreeDateByCode(
        locationPage: LocationPage,
        location: Config.Location
    ) {
        val cookieNag = CookieNagComponent(driver)
        val code = location.placementCode
        locationPage.confirmClaim()
        if (code != null) {
            cookieNag.acceptCookies()
            Thread.sleep(2_000)
            locationPage.enterCodeSegment0(code)
            Thread.sleep(2_000)
            locationPage.searchForFreeDate()
            locationPage.searchForVaccinateDate()
            val bookingPage = BookingPage(driver)
            if (locationPage.hasNoVaccinateDateAvailable() || locationPage.hasVacError()) {
                log.debug { "Correct code, but not free vaccination slots: $location" }
            } else if (bookingPage.isDisplayed() && bookingPage.isDisplayingVaccinationDates()) {
                sendMessageFoundDates(location)

                if (bookingEnabled) {
                    // 1. Select first date possible
                    Thread.sleep(2_000)
                    bookingPage.selectFirstVaccinationDatePair()
                    sendMessage("Selected first vaccination date")
                    log.debug { "Selected first vaccination date..." }
                    Thread.sleep(2_000)
                    takeScreenshot(driver, "impf-bot-vaccination-date-$code.png")
                    bookingPage.submitInputWithText("AUSWÄHLEN")

                    // 2. Open modal to enter personal data
                    if (bookingPage.isDisplayingSecondBookingStep()) {
                        sendMessage("Second booking step is shown")
                        Thread.sleep(2_000)
                        bookingPage.selectEnterPersonalData()
                        log.debug { "Open personal data modal..." }
                    }

                    // 3. Enter personal data
                    if (bookingPage.isDisplayingPersonalDataForm()) {
                        sendMessage("Showing personal data form")
                        enterAndSubmitPersonalDataIntoForm(bookingPage)
                        takeScreenshot(driver, "impf-bot-booking-data-$code.png")
                    }

                    // 4. Submit Vaccination date
                    if (bookingPage.isDisplayingFinalBookingStep()) {
                        sendMessage("All personal data entered, ready to submit!")
                        log.debug { "All personal data entered, ready to submit!" }

                        takeScreenshot(driver, "impf-bot-booking-overview-$code.png")

                        bookingPage.submitFinalBooking()

                        takeScreenshot(driver, "impf-bot-booking-confirmation-$code.png")
                    }
                } else {
                    waitLongForUserInput()
                }
            } else {
                log.debug { "Correct code, we can't see the bookable vaccination dates: $location" }
            }
        }
    }

    private fun enterAndSubmitPersonalDataIntoForm(bookingPage: BookingPage) {
        Thread.sleep(2_000)
        bookingPage.enterSalutation(personalDataSalutation)
        log.info { "Salutation entered..." }
        Thread.sleep(2_000)
        bookingPage.enterFirstname(personalDataFirstname)
        log.info { "Firstname entered..." }
        Thread.sleep(2_000)
        bookingPage.enterLastname(personalDataLastname)
        log.info { "Lastname entered..." }
        Thread.sleep(2_000)
        bookingPage.enterZipCode(personalDataZipcode)
        log.info { "Zipcode entered..." }
        Thread.sleep(2_000)
        bookingPage.enterCity(personalDataCity)
        log.info { "City entered..." }
        Thread.sleep(2_000)
        bookingPage.enterStreet(personalDataStreet)
        log.info { "Street entered..." }
        Thread.sleep(2_000)
        bookingPage.enterHousenumber(personalDataHouseNumber)
        log.info { "Housenumber entered..." }
        Thread.sleep(2_000)
        bookingPage.enterPhone(personalDataMobileNumber)
        log.info { "Phone entered..." }
        Thread.sleep(2_000)
        bookingPage.enterEmail(personalDataEmail)
        log.info { "Email entered..." }
        Thread.sleep(2_000)
        bookingPage.submitInputWithText("Übernehmen")
    }

    private fun takeScreenshot(driver: WebDriver, fileName: String) {
        if (takeScreenshots) {
            val screenshot: File = (driver as TakesScreenshot).getScreenshotAs(OutputType.FILE)
            FileUtils.copyFile(screenshot, File("${outputPath.ifEmpty { FileUtils.getUserDirectoryPath() }}/$fileName"))
        }
    }

    private fun checkClaim(
        locationPage: LocationPage,
        cookieNag: CookieNagComponent,
        location: Config.Location
    ) {
        locationPage.askForClaim()
        Thread.sleep(Config.waitingTimeForBrowser())
        cookieNag.acceptCookies()
        if (locationPage.isFull()) {
            log.debug { "Location: $location is full" }
        } else {
            locationPage.checkCorrectPerson()
            locationPage.enterBirthDate(birthDate)
            locationPage.submitInput()
            Thread.sleep(Config.waitingTimeForBrowser())
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
        Thread.sleep(Config.waitingTimeForUserAction())
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
            if (Config.isSlackEnabled() && Config.readSmsFromSlack) {
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
        for (i in 0..retries) {
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
        val message = "Found free seats in location ${location.name}:${driver.currentUrl}\n" +
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
        if(Config.isAlarmEnabled()){
            Alarm().alert()
        }
        when {
            Config.isSlackEnabled() -> {
                SlackClient().sendMessage(message)
            }
            Config.isTelegramEnabled() -> {
                TelegramClient().sendMessage(message)
            }
            Config.isSendgridEnabled() -> {
                SendgridClient().sendMessage(message)
            }
            Config.isGmailEnabled() -> {
                GmailClient().sendMessage(message)
            }
            Config.isIFTTTEnabled() -> {
                IftttClient().sendMessage(message)
            }
        }
    }

    private fun openMainPage(driver: WebDriver, cookieNag: CookieNagComponent): MainPage {
        val mainPage = MainPage(driver)
        mainPage.open()
        log.debug { "Choose State: " + mainPage.chooseState()?.text }
        cookieNag.acceptCookies()
        mainPage.chooseState()?.click()
        mainPage.chooseStateItem(Config.state)
        return mainPage
    }
}

