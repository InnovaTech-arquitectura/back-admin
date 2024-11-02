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
public class ProfileTest {
    
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
    public void SystemTest_Profile_Complete() {
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

        // Navega a la sección de perfiles
        WebElement profilesButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkPerfiles")));
        profilesButton.click();

        WebElement adminButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile-1")));
        adminButton.click();

        // Crea nuevo empleado

        WebElement addEmployeeButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addEmployee")));
        addEmployeeButton.click();

        WebElement inputName = driver.findElement(By.id("nombre"));
        WebElement inputDocumento = driver.findElement(By.id("documento"));
        WebElement inputMailEmployee = driver.findElement(By.id("email"));
        WebElement inputPasswordEmployee = driver.findElement(By.id("password"));

        inputName.sendKeys("Empleado de prueba");
        inputDocumento.clear();
        inputDocumento.sendKeys("1911");
        inputMailEmployee.sendKeys("prueba@mail.com");
        inputPasswordEmployee.sendKeys("123");

        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createEmployee")));
        createButton.click();

        // Vuelve a la tabla para ver que se cree el empleado
        WebElement adminButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile-1")));
        adminButton2.click();

        WebElement newProfile = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/app-perfil-tipo/body/section/div[2]/table/tbody/tr[2]/td[4]/div/a[1]")));
        String profileId = newProfile.getAttribute("id");
        String[] parts = profileId.split("-");
        String idAfterDash = parts[1];

        // Edita el empleado
        WebElement ediElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-" + idAfterDash)));
        ediElement.click();

        WebElement inputNameEdit = driver.findElement(By.id("nombre"));
        inputNameEdit.clear();
        inputNameEdit.sendKeys("Empleado");

        WebElement saveEmployee = wait.until(ExpectedConditions.elementToBeClickable(By.id("saveEmployee")));
        saveEmployee.click();

        // Vuelve a la tabla para ver que se edite el empleado
        WebElement adminButton3 = wait.until(ExpectedConditions.elementToBeClickable(By.id("profile-1")));
        adminButton3.click();

        // Elimina el empleado
        WebElement deleteEmployee = wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + idAfterDash)));
        deleteEmployee.click();

        WebElement confirmPopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[3]")));
        confirmPopup.click();

        WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        closePopup.click();

        // Para que cargue la página
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit-1")));
    }
    
    @AfterEach
    void tearDown() {
        driver.quit();
    }
}

