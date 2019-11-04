package stepDefinitions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import utils.readExcel;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AmazonStepDefinition {

	WebDriver driver;
	JavascriptExecutor js;
	TakesScreenshot scrShot;
	File srcFile;
	String productName = "";
	String productQuantity = "";
	String productPrice = "";

	@FindBy(id = "twotabsearchtextbox")
	WebElement searchTextBox;

	@FindBy(xpath = "//*[@id=\"nav-search\"]/form/div[2]/div/input")
	WebElement searchButton;

	@FindBy(xpath = "//*[@id=\"a-autoid-0-announce\"]")
	WebElement sortByDropDown;

	@FindBy(xpath = "//*[@id=\"s-result-sort-select_1\"]")
	WebElement sortByLowToHighprice;

	@FindBy(xpath = "//div[contains(@class, 's-result-item') ]")
	List<WebElement> listOfProduct;

	@FindBy(xpath = "//span[contains(@class, ' a-text-normal') ]")
	List<WebElement> listNameOfProduct;

	@FindBy(id = "add-to-cart-button")
	WebElement addTocart;

	@FindBy(linkText = "See All Buying Options")
	WebElement seeAllBuyingOptions;

	@FindBy(xpath = ".//select[@id='quantity']")
	WebElement selectQuantityDropDown;

	@FindBy(name = "submit.addToCart")
	List<WebElement> renewedAddToCart;

	@FindBy(className = "currencyINRFallback")
	List<WebElement> priceOfProduct;

	@FindBy(id = "nav-cart")
	WebElement cart;

	@FindBy(className = "a-size-medium sc-product-title a-text-bold")
	List<WebElement> productNamesInCart;

	@FindBy(xpath = "//select[contains(@name , 'quantity')]")
	List<WebElement> selectQuantityCartPage;

	@FindBy(xpath = "//*[contains(@id,'sc-item-')]/div[4]/div/div[2]/p/span/text()")
	List<WebElement> cartPrices;

	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver","./chromedriver78.exe");
		
		driver = new ChromeDriver();

		js = (JavascriptExecutor) driver;
		scrShot =((TakesScreenshot)driver);

		PageFactory.initElements(driver, this);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
	}

	@Given("^user is signed in on Amazon$")
	public void user_is_signed_in_on_Amazon() throws IOException {

		driver.get("https://www.amazon.in");
		
		//login logic 
		
		//screenshot
		srcFile = scrShot.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile,new File("./signedIn.png"));
	}

	@When("^user adds \"([^\"]*)\" items to the cart$")
	public void user_adds_items_to_the_cart(String quantity) throws IOException{
		
		readExcel obj = new readExcel();
		

	    //Call read file method of the class to read data
	    String[] objects = obj.read("./data.xlsx","AmazonData",Integer.getInteger(quantity));
	    for(int i=0;i<objects.length;i++){
	    	System.out.println(objects[i]);
	    }
	    
	    for(int i=0;i<objects.length;i++){
	    	searchTextBox.sendKeys(objects[i]);
			searchButton.click();
	    }
		
		
		System.out.println("user adding product(s) in cart");
		WebElement productToBuy = listNameOfProduct.get(0);

		productToBuy.click();

		productName = productToBuy.getText();

		System.out.println(productName);

		ArrayList<String> windowsList = new ArrayList<String>(driver.getWindowHandles());
		if (windowsList.size() > 1) {
			for (int i = 0; i < windowsList.size(); i++) {
				System.out.println(windowsList.get(i));
			}
			driver.switchTo().window(windowsList.get(1));
		}

		try {
			seeAllBuyingOptions.click();

			productPrice = priceOfProduct.get(0).getText();
			System.out.println( priceOfProduct.get(0).getText());

		} catch (NoSuchElementException noSuchElementException) {
			System.out.println("no - see all buying");

		}

		try {
			Select quantitySelect = new Select(selectQuantityDropDown);
			int selectOptionsSize = quantitySelect.getOptions().size();

			if (selectOptionsSize >= Integer.getInteger(quantity)) {
				productQuantity = quantity;
				quantitySelect.selectByVisibleText(quantity);
				System.out.println("You got the exact quantity match for " + quantity);
			} else if (selectOptionsSize < Integer.getInteger(quantity)) {
				quantitySelect.selectByIndex(selectOptionsSize - 1);
				quantity = quantitySelect.getOptions().get(selectOptionsSize - 1).getText().toString();
				productQuantity = quantity;
				System.out.println("Only " + quantity + " products are available");
			}
		} catch (Exception exception) {
			productQuantity = "1";
			System.out.println("Only 1 product is available");
		}

		try {
			renewedAddToCart.get(0).click();
			addTocart.click();
		} catch (Exception exception) {
		}

	}
	
	
	@Then("^user should be able to see \"([^\"]*)\" items in the cart$")
	public void user_should_be_able_to_see_items_in_the_cart(String quantity){
		System.out.println("Asserting the cart");
		cart.click();
		// assert the name of product
		System.out.println("1 "+productName);
//		WebDriverWait wait=new WebDriverWait(driver, 20);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'a-size-medium sc-product-title a-text-bold')]")));
//		System.out.println(productNamesInCart.size());
		
//		System.out.println(productNamesInCart.get(0).getText());
//		Assert.assertEquals(productName, productNamesInCart.get(0).getText());

		// assert quantity of product
		Select selectedCart = new Select(selectQuantityCartPage.get(0));
		System.out.println("2 "+productQuantity);
		System.out.println("3 "+selectedCart.getFirstSelectedOption().getText().trim());

		Assert.assertEquals(productQuantity, selectedCart.getFirstSelectedOption().getText().trim());

		// assert price
		System.out.println("4 "+productPrice);
		System.out.println("5 "+cartPrices.get(0));
		Assert.assertEquals(productPrice, cartPrices.get(0));

	}
	
	@And("^user should be able to place the order$")
	public void user_should_be_able_to_place_the_order(){
		
	}

	@After
	public void cleanUp() {
		driver.quit();
	}
}
