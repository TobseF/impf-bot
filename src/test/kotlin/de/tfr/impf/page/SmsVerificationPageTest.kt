package de.tfr.impf.page

import de.tfr.impf.selenium.createDriver
import de.tfr.impf.test.util.getResourceAsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SmsVerificationPageTest {

    private lateinit var page: SmsVerificationPage

    @BeforeEach
    fun beforeEach() {
        val driver = createDriver()
        driver.get(getResourceAsString(this, "SMS-Verifizierung.html"))
        page = SmsVerificationPage(driver)
    }

    @Test
    fun getTitle() {
        assertEquals("SMS Verifizierung", page.title()?.text)
    }

    @Test
    fun isDisplayed() {
        assertTrue(page.isDisplayed())
    }
}