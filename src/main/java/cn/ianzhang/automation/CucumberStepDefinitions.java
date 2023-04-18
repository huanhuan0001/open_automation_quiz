package cn.ianzhang.automation;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CucumberStepDefinitions {
    WebDriver driver;

    @Given("用户打开网页 {string}")
    public void 用户打开网页(String url) {
        // 设置ChromeDriver的路径
        String os = getOsSystem();
        String resourcePath = CucumberStepDefinitions.class.getClassLoader().getResource("").getPath();

        if (os.equals("mac")) {
            System.setProperty("webdriver.chrome.driver", resourcePath + "/chromedriver/mac/chromedriver");
        } else {
            System.setProperty("webdriver.chrome.driver", resourcePath + "/chromedriver/linux/chromedriver");
        }

        driver = new ChromeDriver();
        driver.get(url);
    }

    @When("用户在第一页选择选项组 {string} 并选择选项 {string}")
    public void 用户在第一页选择选项组(String groupName, String option) {
        WebElement selectElement = driver.findElement(By.name(groupName));
        Select select = new Select(selectElement);
        select.selectByVisibleText(option);
    }

    @And("用户点击下一页按钮")
    public void 用户点击下一页按钮() {
        WebElement nextButton = driver.findElement(By.className("next-page"));
        nextButton.click();
    }

    @Then("用户在第二页填写申请日期为当年元旦日期、申请人为自动化、联系方式为 {string}")
    public void 用户在第二页填写申请日期申请人联系方式(String contact) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        WebElement dateInput = driver.findElement(By.name("申请日期"));
        WebElement personInput = driver.findElement(By.name("申请人"));
        WebElement contactInput = driver.findElement(By.name("联系方式"));

        dateInput.sendKeys(formattedDate);
        personInput.sendKeys("自动化");
        contactInput.sendKeys(contact);
    }

    @And("用户截图保存为 {string}")
    public void 用户截图保存为(String filename) {
        // 获取截图
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // 生成保存文件的路径
        String resourcePath = BingSearchAutomation.class.getClassLoader().getResource("").getPath();
        String filePath = resourcePath + "/screenshot/" + filename;

        try {
            // 将截图保存到指定路径
            Files.copy(screenshotFile.toPath(), Paths.get(filePath));
            System.out.println("截图已保存至：" + filePath);
        } catch (IOException e) {
            System.out.println("保存截图时出现异常：" + e.getMessage());
        }
    }

    @And("用户关闭浏览器")
    public void 用户关闭浏览器() {
        driver.quit();
    }

    @And("用户在第三页填写以下内容")
    public void 用户在第三页填写以下内容(io.cucumber.datatable.DataTable dataTable) {
        // 获取第三页的输入框和提交按钮
        WebElement unitField = driver.findElement(By.xpath("//input[@name='报备单位']"));
        WebElement numField = driver.findElement(By.xpath("//input[@name='在岗人数']"));
        WebElement dateField = driver.findElement(By.xpath("//input[@name='报备日期']"));
        WebElement contactField = driver.findElement(By.xpath("//input[@name='单位负责人']"));
        WebElement phoneField = driver.findElement(By.xpath("//input[@name='联系方式']"));
        WebElement planField = driver.findElement(By.xpath("//textarea[@name='疫情防控方案']"));
        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(),'提交')]"));

        // 从DataTable中获取填写的内容
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = rows.get(0);

        // 填写内容到输入框
        unitField.sendKeys(data.get("报备单位"));
        numField.sendKeys(data.get("在岗人数"));
        dateField.sendKeys(data.get("报备日期"));
        contactField.sendKeys(data.get("单位负责人"));
        phoneField.sendKeys(data.get("联系方式"));
        planField.sendKeys(data.get("疫情防控方案"));

        // 截图保存第三页
        截图保存为("第三页截图.png");

        // 点击提交按钮
        submitButton.click();
    }

    @Then("用户判断提交成功并进行截图")
    public void 用户判断提交成功并进行截图() {
        // 判断提交成功
        WebElement successMessage = driver.findElement(By.xpath("//div[@class='alert alert-success']"));
        assertTrue(successMessage.isDisplayed());

        // 截图保存提交结果页
        截图保存为("提交结果页截图.png");
    }

    // 辅助方法：截图保存为
    private void 截图保存为(String filename) {
        // 获取截图
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // 生成保存文件的路径
        String filePath = "path/to/screenshot/directory/" + filename;

        try {
            // 将截图保存到指定路径
            Files.copy(screenshotFile.toPath(), Paths.get(filePath));
            System.out.println("截图已保存至：" + filePath);
        } catch (IOException e) {
            System.out.println("保存截图时出现异常：" + e);
        }
    }

    // 辅助方法：断言为真
    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("断言失败");
        }
    }

    // 辅助方法：初始化WebDriver
    private void 初始化WebDriver() {
        // 设置ChromeDriver路径
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // 创建ChromeDriver实例
        driver = new ChromeDriver();

        // 最大化窗口
        driver.manage().window().maximize();
    }

    // 辅助方法：关闭WebDriver
    private void 关闭WebDriver() {
        // 关闭浏览器窗口
        driver.quit();
    }

    private static String getOsSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            return "mac";
        } else if (osName.contains("linux")) {
            return "linux";
        } else {
            return "other";
        }
    }
}
