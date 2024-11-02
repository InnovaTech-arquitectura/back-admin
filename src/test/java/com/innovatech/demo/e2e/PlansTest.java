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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlansTest {
    
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

        // Navega a la sección de agregar planes
        WebElement plansButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkPlanes")));
        plansButton.click();

        WebElement addPlanButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addPlan")));
        addPlanButton.click();

        // Ingresa los datos del plan
        WebElement inputName = driver.findElement(By.id("nombre"));
        WebElement inputPrice = driver.findElement(By.id("precio"));
        WebElement inputFunc1 = driver.findElement(By.id("check-1"));
        WebElement inputFunc2 = driver.findElement(By.id("check-2"));
        WebElement inputFunc3 = driver.findElement(By.id("check-3"));
        WebElement inputFunc4 = driver.findElement(By.id("check-4"));

        inputName.sendKeys("Plan de prueba");
        inputPrice.sendKeys("100");
        inputFunc1.click();
        inputFunc2.click();
        inputFunc3.click();
        inputFunc4.click();

        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createButton")));
        createButton.click();

        //Espera que cargue la pagina principal
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plan-1")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plan-1")));
        driver.navigate().refresh(); // Porque selenium es muy rápido       
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plan-1")));  
        driver.navigate().refresh(); // Porque selenium es muy rápido       

        WebElement newCard = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/app-ver-planes/body/section/div[2]/div[4]")));
        String planId = newCard.getAttribute("id");
        String[] parts = planId.split("-");
        String idAfterDash = parts[1];

        // Edita el plan
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-" + idAfterDash)));
        editButton.click();

        WebElement inputNameEdit = driver.findElement(By.id("nombre"));
        inputNameEdit.clear();
        inputNameEdit.sendKeys("Plan de prueba editado");

        WebElement savePlan = wait.until(ExpectedConditions.elementToBeClickable(By.id("savePlan")));
        savePlan.click();

        //Espera que cargue la pagina principal
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plan-1")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plan-" + idAfterDash)));

        // Elimina el plan
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + idAfterDash)));
        deleteButton.click();

        WebElement confirmPopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[3]")));
        confirmPopup.click();

        WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        closePopup.click();

        //Para que cargue la página
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("plan-1")));
    }
    
    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
