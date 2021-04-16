package de.tfr.impf.page

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory

abstract class AbstractPage(protected val driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    fun findByOrNull(xPath: String): WebElement? {
        return findAll(xPath).firstOrNull()
    }

    fun findBy(xPath: String): WebElement {
        return driver.findElement(By.xpath(xPath))
    }

    fun findByLazy(xPath: String): Lazy<WebElement> {
        return lazy { findBy(xPath) }
    }

    fun lazyFindAll(xPath: String): Lazy<MutableList<WebElement>> {
        return lazy { findAll(xPath) }
    }

    fun findAll(xPath: String): MutableList<WebElement> = driver.findElements(By.xpath(xPath))

}