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
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BazarTest {
    
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
    public void SystemTest_Bazar_Complete() {
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

        // Navega a la sección de bazares
        WebElement bazarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkBazares")));
        bazarButton.click();

        WebElement addBazarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createBazar")));
        addBazarButton.click();

        // Crea el bazar 

        WebElement nombreInput = driver.findElement(By.id("nombre"));
        WebElement gananciasInput = driver.findElement(By.id("ganancias"));
        WebElement dateInput = driver.findElement(By.id("date"));
        WebElement costoLocalInput = driver.findElement(By.id("costo"));
        WebElement quotaInput = driver.findElement(By.id("quota"));
        WebElement modalidadSelect = driver.findElement(By.name("modalidad"));
        WebElement lugarInput = driver.findElement(By.id("lugar"));
        
        nombreInput.sendKeys("Bazar prueba");
        gananciasInput.clear();
        gananciasInput.sendKeys("5000");
        dateInput.sendKeys("19-11-2024");
        costoLocalInput.clear();
        costoLocalInput.sendKeys("300");
        quotaInput.clear();
        quotaInput.sendKeys("100");

        Select modalidadDropdown = new Select(modalidadSelect);
        modalidadDropdown.selectByVisibleText("Presencial");

        lugarInput.sendKeys("Centro de Convenciones");

        WebElement createBazarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createBazar")));
        createBazarButton.click();

        WebElement newBazar = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/app-ver-bazares/body/section/div[2]/table/tbody/tr[6]/td[6]/a[2]")));
        String bazarId = newBazar.getAttribute("id");
        String[] parts = bazarId.split("-");
        String idAfterDash = parts[1];

        // Vuelve a la tabla para ver que se cree el bazar
        WebElement editBazar = wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-" + idAfterDash)));
        editBazar.click();

        // Edita el bazar
        WebElement nombreInputEdit = driver.findElement(By.id("name"));
        nombreInputEdit.clear();
        nombreInputEdit.sendKeys("Bazar editado");

        WebElement saveBazar = wait.until(ExpectedConditions.elementToBeClickable(By.id("saveBazar")));
        saveBazar.click();

        // Vuelve a la tabla para ver que se edite el bazar
        wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + idAfterDash)));
        driver.navigate().refresh();

        // Elimina el bazar
        WebElement deleteBazar = wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + idAfterDash)));
        deleteBazar.click();

        driver.navigate().refresh();

        // Para que cargue la página
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit-1")));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
