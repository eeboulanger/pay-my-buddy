package com.paymybuddy.webapp.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-e2e.properties")
public class PayMyBuddyE2E {

    @LocalServerPort
    private Integer port;
    private WebDriver webDriver = null;
    private String basUrl;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUpDriver() {
        webDriver = new ChromeDriver();
        basUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Nested
    class SignUpTests {

        @Test
        @DisplayName("Given the user email doesn't exist already and the fields are valid, " +
                "when sign up new user, " +
                "then create new account, redirect to login and display success message")
        public void signUpFormTest() {
            //GIVEN
            webDriver.get(basUrl + "/signup");
            WebElement emailField = webDriver.findElement(By.id("email"));
            WebElement usernameField = webDriver.findElement(By.id("username"));
            WebElement passwordField = webDriver.findElement(By.id("password"));
            WebElement submitButton = webDriver.findElement(By.id("submitButton"));

            //WHEN
            emailField.sendKeys("new_user@mail.com");
            usernameField.sendKeys("New user");
            passwordField.sendKeys("ValidPassword@123");
            submitButton.click();

            //THEN
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlToBe(basUrl + "/login"));

            String currentUrl = webDriver.getCurrentUrl();
            assertEquals(basUrl + "/login", currentUrl);
            WebElement successMessage = webDriver.findElement(By.id("successMessage"));
            assertEquals("Votre compte a été créé avec succès ! Veuillez vous connecter", successMessage.getText());
        }

        @Test
        @DisplayName("Given the user email exists already and the fields are valid, " +
                "when sign up new user, " +
                "then create new account, redirect to login and display success message")
        public void signUpForm_withExistingUserTest() {
            //GIVEN
            webDriver.get(basUrl + "/signup");
            WebElement emailField = webDriver.findElement(By.id("email"));
            WebElement usernameField = webDriver.findElement(By.id("username"));
            WebElement passwordField = webDriver.findElement(By.id("password"));
            WebElement submitButton = webDriver.findElement(By.id("submitButton"));

            //WHEN
            emailField.sendKeys("john_doe@mail.com");
            usernameField.sendKeys("New user");
            passwordField.sendKeys("ValidPassword@123");
            submitButton.click();

            //THEN
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.urlToBe(basUrl + "/signup"));

            String currentUrl = webDriver.getCurrentUrl();
            assertEquals(basUrl + "/signup", currentUrl);
            WebElement error = webDriver.findElement(By.id("errorMessage"));
            assertEquals("Email déjà utilisé", error.getText());
        }

    }

    @Nested
    class LoggedInUserTests {
        private WebDriverWait wait;

        @BeforeEach
        public void logInUser() {
            //LOGIN AS EXISTING USER
            webDriver.get(basUrl);
            WebElement email = webDriver.findElement(By.id("email"));
            WebElement password = webDriver.findElement(By.id("password"));
            WebElement login = webDriver.findElement(By.id("loginButton"));

            email.sendKeys("jimmie_doe@mail.com");
            password.sendKeys("1234@Abcd");
            login.click();

            wait = new WebDriverWait(webDriver, Duration.ofSeconds(3));
        }

        @Test
        @DisplayName("Given the fields are valid, when updating user, then update user and display success message")
        public void updateProfileTest() {
            //GIVEN
            WebElement passwordField = webDriver.findElement(By.id("password"));
            WebElement username = webDriver.findElement(By.id("username"));
            WebElement submit = webDriver.findElement(By.id("submitButton"));

            //WHEN
            username.clear();
            username.sendKeys("Jim"); //Update username
            passwordField.sendKeys("1234@Abcd");
            submit.click();

            //THEN
            final WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("successMessage")));
            final String success = successMessage.getText();
            assertEquals("Votre profil a été modifié avec succès", success);
        }

        @Test
        @DisplayName("Given the user email exists, when add new user connection, " +
                "then add connection to user's list and display success message")
        public void addUserConnectionTest() {
            //GIVEN
            webDriver.get(basUrl + "/connections");
            WebElement email = webDriver.findElement(By.id("email"));
            WebElement submit = webDriver.findElement(By.id("submitButton"));

            //WHEN
            email.sendKeys("jane_doe@mail.com");
            submit.click();

            //THEN
            final WebElement success = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("successMessage")));
            final String successMessage = success.getText();
            assertEquals("La relation a été ajoutée avec succès !", successMessage);
        }

        @Test
        @DisplayName("Given fields are valid, when transfer money, then create new transaction and display siccess message")
        public void userPaymentTest() {
            //GIVEN
            webDriver.get(basUrl + "/payments");
            WebElement amount = webDriver.findElement(By.id("amount"));
            WebElement description = webDriver.findElement(By.id("description"));
            WebElement dropDownList = webDriver.findElement(By.id("dropDownList"));
            WebElement submit = webDriver.findElement(By.id("submitButton"));

            //WHEN
            Select select = new Select(dropDownList);
            select.selectByValue("1");
            amount.clear();
            amount.sendKeys("2");
            description.sendKeys("Gift");
            submit.click();

            //THEN
            final WebElement success = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("successMessage")));
            final String successMessage = success.getText();
            assertEquals("Votre transaction a été envoyée avec succès !", successMessage);
        }
    }
}
