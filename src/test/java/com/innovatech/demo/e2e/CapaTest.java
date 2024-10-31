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
public class CapaTest {
    
    private final String BASE_URL = "http://localhost:4200/";

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
    public void SystemTest_Capacitaciones_Complete() {
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

        // Navega a la sección de capacitaciones
        WebElement capaButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkCapacitaciones")));
        capaButton.click();

        // Crea la capacitación
        WebElement addCapaButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addCapacitation")));
        addCapaButton.click();

        WebElement tituloInput = driver.findElement(By.id("contenido"));
        WebElement descripcionInput = driver.findElement(By.name("descripcion"));
        WebElement fechaInput = driver.findElement(By.id("fecha"));
        WebElement modalidadSelect = driver.findElement(By.id("modalidad"));
        WebElement direccionInput = driver.findElement(By.id("direccion"));
        WebElement cuposInput = driver.findElement(By.id("cupos"));

        tituloInput.sendKeys("Capacitación prueba");
        descripcionInput.sendKeys("Descripción breve de la capacitación de seguridad.");
        fechaInput.sendKeys("19-11-2024");

        Select modalidadDropdown = new Select(modalidadSelect);
        modalidadDropdown.selectByVisibleText("Presencial");

        direccionInput.sendKeys("Av. Central 123 - Centro de Capacitación");
        cuposInput.clear();
        cuposInput.sendKeys("50");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createCapa")));
        submitButton.click();

        // Vuelve a la tabla para ver que se cree la capacitación
        WebElement newCapa = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/app-ver-capacitaciones/body/section/div[3]/table/tbody/tr[3]/td[4]/div/a[1]")));

        String capaId = newCapa.getAttribute("id");
        String[] parts = capaId.split("-");
        String idAfterDash = parts[1];
        
        // Edita la capacitación
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-" + idAfterDash)));
        editButton.click();

        WebElement tituloInputEdit = driver.findElement(By.id("contenido"));
        tituloInputEdit.clear();
        tituloInputEdit.sendKeys("Capacitación prueba editada");

        WebElement saveCapa = wait.until(ExpectedConditions.elementToBeClickable(By.id("saveCapa")));
        saveCapa.click();

        // Elimina la capacitación
        WebElement deleteCapa = wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + idAfterDash)));
        deleteCapa.click();

        WebElement confirmPopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        confirmPopup.click();

        WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        closePopup.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-1")));
    }
    
    @AfterEach
    void tearDown() {
        driver.quit();
    }
}

