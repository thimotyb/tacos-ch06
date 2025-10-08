package tacos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Disabled("Ignore for now...deal with security implications in tests.")
public class DesignAndOrderTacosBrowserTest {

  private static HtmlUnitDriver browser;

  @LocalServerPort
  private int port;

  @Autowired
  TestRestTemplate rest;

  @BeforeAll
  public static void setup() {
    browser = new HtmlUnitDriver();
    browser.manage().timeouts()
        .implicitlyWait(Duration.ofSeconds(10));
  }

  @AfterAll
  public static void closeBrowser() {
    browser.quit();
  }

  @Test
  public void testDesignATacoPage_HappyPath() throws Exception {
    browser.get(homePageUrl());
    clickDesignATaco();
    assertLandedOnLoginPage();
    doRegistration("testuser", "testpassword");
    assertLandedOnLoginPage();
    doLogin("testuser", "testpassword");
    assertDesignPageElements();
    buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
    clickBuildAnotherTaco();
    buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
    doLogout();
  }

  @Test
  public void testDesignATacoPage_EmptyOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignATaco();
    assertLandedOnLoginPage();
    doRegistration("testuser2", "testpassword");
    doLogin("testuser2", "testpassword");
    assertDesignPageElements();
    buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
    submitEmptyOrderForm();
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
    doLogout();
  }

  @Test
  public void testDesignATacoPage_InvalidOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignATaco();
    assertLandedOnLoginPage();
    doRegistration("testuser3", "testpassword");
    doLogin("testuser3", "testpassword");
    assertDesignPageElements();
    buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
    submitInvalidOrderForm();
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
    doLogout();
  }

  //
  // Browser test action methods
  //
  private void buildAndSubmitATaco(String name, String... ingredients) {
    assertDesignPageElements();

    for (String ingredient : ingredients) {
      browser.findElement(By.cssSelector("input[value='" + ingredient + "']")).click();
    }
    browser.findElement(By.cssSelector("input#name")).sendKeys(name);
    browser.findElement(By.cssSelector("form#tacoForm")).submit();
  }

  private void assertLandedOnLoginPage() {
    assertEquals(loginPageUrl(), browser.getCurrentUrl());
  }

  private void doRegistration(String username, String password) {
    browser.findElement(By.linkText("here")).click();
    assertEquals(registrationPageUrl(), browser.getCurrentUrl());
    browser.findElement(By.name("username")).sendKeys(username);
    browser.findElement(By.name("password")).sendKeys(password);
    browser.findElement(By.name("confirm")).sendKeys(password);
    browser.findElement(By.name("fullname")).sendKeys("Test McTest");
    browser.findElement(By.name("street")).sendKeys("1234 Test Street");
    browser.findElement(By.name("city")).sendKeys("Testville");
    browser.findElement(By.name("state")).sendKeys("TX");
    browser.findElement(By.name("zip")).sendKeys("12345");
    browser.findElement(By.name("phone")).sendKeys("123-123-1234");
    browser.findElement(By.cssSelector("form#registerForm")).submit();
  }


  private void doLogin(String username, String password) {
    browser.findElement(By.name("username")).sendKeys(username);
    browser.findElement(By.name("password")).sendKeys(password);
    browser.findElement(By.cssSelector("form#loginForm")).submit();
  }

  private void doLogout() {
    WebElement logoutForm = browser.findElement(By.cssSelector("form#logoutForm"));
    if (logoutForm != null) {
      logoutForm.submit();
    }
  }

  private void assertDesignPageElements() {
    assertEquals(designPageUrl(), browser.getCurrentUrl());
    List<WebElement> ingredientGroups = browser.findElements(By.className("ingredient-group"));
    assertEquals(5, ingredientGroups.size());

    WebElement wrapGroup = browser.findElement(By.cssSelector("div.ingredient-group#wraps"));
    List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
    assertEquals(2, wraps.size());
    assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla");
    assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla");

    WebElement proteinGroup = browser.findElement(By.cssSelector("div.ingredient-group#proteins"));
    List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
    assertEquals(2, proteins.size());
    assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef");
    assertIngredient(proteinGroup, 1, "CARN", "Carnitas");

    WebElement cheeseGroup = browser.findElement(By.cssSelector("div.ingredient-group#cheeses"));
    List<WebElement> cheeses = cheeseGroup.findElements(By.tagName("div"));
    assertEquals(2, cheeses.size());
    assertIngredient(cheeseGroup, 0, "CHED", "Cheddar");
    assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack");

    WebElement veggieGroup = browser.findElement(By.cssSelector("div.ingredient-group#veggies"));
    List<WebElement> veggies = veggieGroup.findElements(By.tagName("div"));
    assertEquals(2, veggies.size());
    assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes");
    assertIngredient(veggieGroup, 1, "LETC", "Lettuce");

    WebElement sauceGroup = browser.findElement(By.cssSelector("div.ingredient-group#sauces"));
    List<WebElement> sauces = sauceGroup.findElements(By.tagName("div"));
    assertEquals(2, sauces.size());
    assertIngredient(sauceGroup, 0, "SLSA", "Salsa");
    assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream");
  }


  private void fillInAndSubmitOrderForm() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    fillField("input#deliveryName", "Ima Hungry");
    fillField("input#deliveryStreet", "1234 Culinary Blvd.");
    fillField("input#deliveryCity", "Foodsville");
    fillField("input#deliveryState", "CO");
    fillField("input#deliveryZip", "81019");
    fillField("input#ccNumber", "4111111111111111");
    fillField("input#ccExpiration", "10/19");
    fillField("input#ccCVV", "123");
    browser.findElement(By.cssSelector("form#orderForm")).submit();
  }

  private void submitEmptyOrderForm() {
    assertEquals(currentOrderDetailsPageUrl(), browser.getCurrentUrl());
    // clear fields automatically populated from user profile
    fillField("input#deliveryName", "");
    fillField("input#deliveryStreet", "");
    fillField("input#deliveryCity", "");
    fillField("input#deliveryState", "");
    fillField("input#deliveryZip", "");
    browser.findElement(By.cssSelector("form#orderForm")).submit();

    assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertEquals(9, validationErrors.size());
    assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
    assertTrue(validationErrors.contains("Delivery name is required"));
    assertTrue(validationErrors.contains("Street is required"));
    assertTrue(validationErrors.contains("City is required"));
    assertTrue(validationErrors.contains("State is required"));
    assertTrue(validationErrors.contains("Zip code is required"));
    assertTrue(validationErrors.contains("Not a valid credit card number"));
    assertTrue(validationErrors.contains("Must be formatted MM/YY"));
    assertTrue(validationErrors.contains("Invalid CVV"));
  }

  private List<String> getValidationErrorTexts() {
    List<WebElement> validationErrorElements = browser.findElements(By.className("validationError"));
    List<String> validationErrors = validationErrorElements.stream()
        .map(el -> el.getText())
        .collect(Collectors.toList());
    return validationErrors;
  }

  private void submitInvalidOrderForm() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    fillField("input#deliveryName", "I");
    fillField("input#deliveryStreet", "1");
    fillField("input#deliveryCity", "F");
    fillField("input#deliveryState", "C");
    fillField("input#deliveryZip", "8");
    fillField("input#ccNumber", "1234432112344322");
    fillField("input#ccExpiration", "14/91");
    fillField("input#ccCVV", "1234");
    browser.findElement(By.cssSelector("form#orderForm")).submit();

    assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertEquals(4, validationErrors.size());
    assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
    assertTrue(validationErrors.contains("Not a valid credit card number"));
    assertTrue(validationErrors.contains("Must be formatted MM/YY"));
    assertTrue(validationErrors.contains("Invalid CVV"));
  }

  private void fillField(String fieldName, String value) {
    WebElement field = browser.findElement(By.cssSelector(fieldName));
    field.clear();
    field.sendKeys(value);
  }

  private void assertIngredient(WebElement ingredientGroup,
                                int ingredientIdx, String id, String name) {
    List<WebElement> proteins = ingredientGroup.findElements(By.tagName("div"));
    WebElement ingredient = proteins.get(ingredientIdx);
    assertEquals(id,
        ingredient.findElement(By.tagName("input")).getAttribute("value"));
    assertEquals(name,
        ingredient.findElement(By.tagName("span")).getText());
  }

  private void clickDesignATaco() {
    assertEquals(homePageUrl(), browser.getCurrentUrl());
    browser.findElement(By.cssSelector("a[id='design']")).click();
  }

  private void clickBuildAnotherTaco() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    browser.findElement(By.cssSelector("a[id='another']")).click();
  }


  //
  // URL helper methods
  //
  private String loginPageUrl() {
    return homePageUrl() + "login";
  }

  private String registrationPageUrl() {
    return homePageUrl() + "register";
  }

  private String designPageUrl() {
    return homePageUrl() + "design";
  }

  private String homePageUrl() {
    return "http://localhost:" + port + "/";
  }

  private String orderDetailsPageUrl() {
    return homePageUrl() + "orders";
  }

  private String currentOrderDetailsPageUrl() {
    return homePageUrl() + "orders/current";
  }

}
