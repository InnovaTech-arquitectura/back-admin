package com.innovatech.demo.e2e;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
public class PubliTest {
    
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

        // Navega a la sección de agregar banner 
        WebElement publiButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("linkPublicidad")));
        publiButton.click();

        WebElement addBannerButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addBanner")));
        addBannerButton.click();

        // Ingresa los datos del plan
        WebElement inputTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("titleDescription")));
        WebElement uploadElement = driver.findElement(By.id("imageUpload"));

        inputTitle.sendKeys("Banner de prueba");
        
        String projectPath = System.getProperty("user.dir");
        String filePath = projectPath + "\\src\\test\\java\\com\\innovatech\\demo\\e2e\\bannerTest.jpg";
        
        uploadElement.sendKeys(filePath);

        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createBanner")));
        createButton.click();

        WebElement confirmPopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        confirmPopup.click();

        // Edita el banner
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-ver-banners/body/section/div[2]/table/tbody/tr/td[5]/a[2]")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", editButton);

        WebElement inputTitleEdit = wait.until(ExpectedConditions.elementToBeClickable(By.id("titleDescription")));
        inputTitleEdit.clear();
        inputTitleEdit.sendKeys("Prueba editado");

        String filePathEdit = projectPath + "\\src\\test\\java\\com\\innovatech\\demo\\e2e\\bannerEditTest.jpg";
        
        WebElement uploadElementEdit = driver.findElement(By.id("imageUpload"));
        uploadElementEdit.sendKeys(filePathEdit);

        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("saveBanner")));
        saveButton.click();

        // Elimina el banner
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/app-root/app-ver-banners/body/section/div[2]/table/tbody/tr/td[5]/a[1]")));
        js.executeScript("arguments[0].click();", deleteButton);

        WebElement confirmDelete = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        confirmDelete.click();

        WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[6]/button[1]")));
        closePopup.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("addBanner")));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
