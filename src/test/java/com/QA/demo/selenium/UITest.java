package com.QA.demo.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UITest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode for CI
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + UUID.randomUUID());



        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginFormElementsPresent() {
        driver.get("http://localhost:8080/index.html");

        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.tagName("button"));

        assertTrue(username.isDisplayed());
        assertTrue(password.isDisplayed());
        assertEquals("Login", loginButton.getText());
    }

    @Test
    public void testAddItem() {
        driver.get("http://localhost:8080/index.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Wait for input field to be visible
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("itemInput")));

            // Wait for add button to be clickable
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addItemBtn")));

            input.sendKeys("Test Item");
            addButton.click();

            // Wait for the new list item to appear
            WebElement listItem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='itemList']/li")));

            System.out.println("List item text: " + listItem.getText());
            assertEquals("Test Item", listItem.getText());

        } catch (Exception e) {
            System.out.println("❌ Element not found or test failed: " + e.getMessage());
            System.out.println("📄 Page source:\n" + driver.getPageSource());
            fail("Test failed due to missing element or unexpected page state.");
        }
    }


}
