package com.innovatech.demo.e2e;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CouponTest {
    
    private final String BASE_URL = "http://10.43.101.107/";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void init() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--disable-extensions");

        this.driver = new ChromeDriver(chromeOptions);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test
    public void SystemTest_Plan_Complete() {
        //Busca pagina y la maximiza
        driver.get(BASE_URL + "login");
        driver.manage().window().maximize();

        //Ingresa los datos e inicia sessión
        WebElement inputMail = driver.findElement(By.id("email"));
        WebElement inputPassword = driver.findElement(By.id("password"));

        inputMail.sendKeys("admin@example.com");
        inputPassword.sendKeys("password123");

        String pathButtonLogin = "/html/body/app-root/app-inicio-sesion/div/div[2]/form/button";
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(pathButtonLogin)));
        loginButton.click();

        // Navega a la sección de agregar cupones
        WebElement plansButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkPlanes")));
        plansButton.click();

        WebElement addCouponButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("cuponesBtn")));
        addCouponButton.click();

        WebElement createCouponButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createCoupon")));
        createCouponButton.click();

        // Ingresa los datos del cupón
        WebElement inputDesc = wait.until(ExpectedConditions.elementToBeClickable(By.id("descripcion")));
        WebElement inputDate = wait.until(ExpectedConditions.elementToBeClickable(By.id("expiration")));
        WebElement inputPeriod = wait.until(ExpectedConditions.elementToBeClickable(By.id("period")));
        WebElement inputPlan = wait.until(ExpectedConditions.elementToBeClickable(By.id("plan")));
        WebElement inputFunc = wait.until(ExpectedConditions.elementToBeClickable(By.id("check-1")));

        inputDesc.sendKeys("Cupón de prueba");
        inputDate.sendKeys("19-11-2024");
        inputPeriod.clear();
        inputPeriod.sendKeys("30");
        inputPlan.sendKeys("Plan de prueba");
        Select selectPlan = new Select(inputPlan);
        selectPlan.selectByIndex(1);
        inputFunc.click();

        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createButton")));
        createButton.click();

        //Espera que cargue la pagina principal
        WebElement editButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/app-ver-cupones/body/section/div[2]/table/tbody/tr/td[4]/div/a[1]")));
        editButton.click();

        // Edita el cupón
        WebElement editDesc = wait.until(ExpectedConditions.elementToBeClickable(By.id("descripcion")));
        editDesc.clear();
        editDesc.sendKeys("Cupón de prueba editado");

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createButton")));
        saveButton.click();

        // Elimina el cupón
        WebElement deleteButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/app-ver-cupones/body/section/div[2]/table/tbody/tr/td[4]/div/a[2]")));
        deleteButton.click();

        WebElement confirmPopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[3]")));
        confirmPopup.click();

        WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        closePopup.click();

        //Espera que cargue la pagina principal
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createCoupon")));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
