package com.mitrais.cdc.blogmicroservices.frontendstepdefinition.posts;

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

public class PostsStepDefinition {

    WebDriver webDriver;

    @Given("User click Create Blog Menu")
    public void user_click_Create_Blog_Menu() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        System.out.println("Path:"+System.getProperty("webdriver.chrome.driver"));
        webDriver = new ChromeDriver();
        webDriver.get("http://localhost:3000/blog/create");

        webDriver.findElement(By.xpath("//*[@name='username']")).sendKeys("admin");
        webDriver.findElement(By.xpath("//*[@name='password']")).sendKeys("admin123");
        webDriver.findElement(By.xpath("//*[@id='submit']")).click();
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@name='title']")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='category']")).isDisplayed());
       // Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='content']")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.className("public-DraftEditor-content")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='createblog']")).isDisplayed());
    }

    @When("User on Create Blog page, insert new blog with title (.*) with category (.*) and blog (.*)")
    public void user_on_Create_Blog_page_insert_new_blog_with_title_Oracle_OSB_with_category_Enterprise_Application_Integration_and_blog_testblog(String title, String category, String blog) {
        webDriver.findElement(By.xpath("//*[@name='title']")).sendKeys("Oracle OSB");
        webDriver.findElement(By.xpath("//*[@id='category']")).sendKeys("Enterprise Application Integration");
        webDriver.findElement(By.className("public-DraftEditor-content")).sendKeys("testblog");
        webDriver.findElement(By.xpath("//*[@id='createblog']")).click();
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Then("Blog created successfully, user will direct to home page")
    public void blog_created_successfully_user_will_direct_to_home_page() {
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='data']")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='category-dropdown']")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='new-posting']")).isDisplayed());
        Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id='my-posting']")).isDisplayed());
    }

    @Then("The created blog will be found on that Home Page")
    public void the_created_blog_will_be_found_on_that_Home_Page() {
        List<WebElement> rows = webDriver.findElements(By.xpath("//*[@id=\"data\"]/div/div/h3"));
        System.out.println("No. of rows: "+ rows.size());
        int newRow =  rows.size();
        String title = webDriver.findElement(By.xpath("//*[@id=\"data\"]/div["+newRow+"]/div/h3")).getText();
        String content = webDriver.findElement(By.xpath("//*[@id=\"data\"]/div["+newRow+"]/div/p[1]")).getText();
        assertThat("Oracle OSB", is(title));
        assertThat("testblog", is(content));
    }
}
