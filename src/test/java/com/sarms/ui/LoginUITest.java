package com.sarms.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class LoginUITest {

    private WebDriver driver;
    private Process applicationProcess;
    private Robot robot;

    @BeforeEach
    void setUp() throws Exception {
        // Start the SARMS application
        applicationProcess = Runtime.getRuntime().exec("java -jar target/sarms-1.0-SNAPSHOT.jar");

        // Allow application to start
        Thread.sleep(2000);

        // Set up Robot for keyboard input
        robot = new Robot();
    }

    @Test
    void testLoginAsStudent() throws Exception {
        // Type username
        typeText("S001");

        // Tab to password field
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        // Type password
        typeText("student123");

        // Press Enter to login
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        // Wait for login to complete
        Thread.sleep(1000);

        // Visual verification or screenshot comparison would be needed here
        // This is difficult without a proper UI testing framework for Swing
    }

    private void typeText(String text) {
        for (char c : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            robot.delay(100);
        }
    }

    @AfterEach
    void tearDown() {
        if (applicationProcess != null) {
            applicationProcess.destroy();
        }
    }
}