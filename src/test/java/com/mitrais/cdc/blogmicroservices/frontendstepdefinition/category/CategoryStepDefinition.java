package com.mitrais.cdc.blogmicroservices.frontendstepdefinition.category;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CategoryStepDefinition {

    WebDriver webDriver;

    @Given("User Click menu Create Blog Category")
    public void user_Click_menu_Create_Blog_Category() {

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        System.out.println("Path:"+System.getProperty("webdriver.chrome.driver"));
        webDriver = new ChromeDriver();
        webDriver.get("http://admin:admin123@localhost:3000/category/create");

        webDriver.findElement(By.xpath("//*[@name='username']")).sendKeys("admin");
        webDriver.findElement(By.xpath("//*[@name='password']")).sendKeys("admin123");
        webDriver.findElement(By.xpath("//*[@id='submit']")).click();
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

    }

    @When("User has login, he will direct to create blog category page")
    public void user_has_login_he_will_direct_to_create_blog_category_page() {
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@name='category']")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@name='description']")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='createcategory']")).isDisplayed());
    }

    @Then("User insert Category Name (.*) and Description (.*)")
    public void user_insert_Category_Name_Microcontroller_and_Description_Description(String category, String description) {
        webDriver.findElement(By.xpath("//*[@name='category']")).sendKeys(category);
        webDriver.findElement(By.xpath("//*[@name='description']")).sendKeys(description);
        webDriver.findElement(By.xpath("//*[@id='createcategory']")).click();
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Then("After successfully create new category, user will direct to Category List page")
    public void after_successfully_create_new_category_user_will_direct_to_Category_List_page() {
       Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='categorylist']")).isDisplayed());
    }

    @Then("Category Name (.*) and Description (.*) will exist in that Category List Page.")
    public void category_Name_Microcontroller_and_Description_Description_will_exist_in_that_Category_List_Page(String category, String description) {
        List<WebElement> rows = webDriver.findElements(By.xpath("//*[@id=\"root\"]/div/div/div/main/div/div/div[3]/div/div/div/table/tbody/tr/td[1]"));
        System.out.println("No. of rows: "+ rows.size());
        int newRow =  rows.size()+1;
        String categoryName = webDriver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/main/div/div/div[3]/div/div/div/table/tbody/tr["+newRow+"]/td[2]")).getText();
        String catDescription = webDriver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/main/div/div/div[3]/div/div/div/table/tbody/tr["+newRow+"]/td[3]")).getText();
        System.out.println("Username:"+categoryName);
        assertThat(category, is(categoryName));
        assertThat(description, is(catDescription));
    }
}
