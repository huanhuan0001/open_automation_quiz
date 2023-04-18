package cn.ianzhang.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BingSearchAutomation {
    public static void main(String[] args) {
        // 设置ChromeDriver的路径
        String os = getOsSystem();
        String resourcePath = BingSearchAutomation.class.getClassLoader().getResource("").getPath();

        if (os.equals("mac")) {
            System.setProperty("webdriver.chrome.driver", resourcePath + "/chromedriver/mac/chromedriver");
        } else {
            System.setProperty("webdriver.chrome.driver", resourcePath + "/chromedriver/linux/chromedriver");
        }

        // 创建 ChromeDriver 实例
        WebDriver driver = new ChromeDriver();

        try {
            // 打开网页
            driver.get("https://www.ianzhang.cn/bing/");

            driver.switchTo().alert().accept();

            //---------- 搜索关键词 "赵欢欢"--------
            WebDriverWait wait = new WebDriverWait(driver, 30);

            // 先找到iframe
            WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("bing")));
            driver.switchTo().frame(iframe);

            System.out.println("-------------<搜索 赵欢欢>---------------");
            search(driver, "赵欢欢");
            Thread.sleep(7000);

            // 获取第二页搜索结果
           /* WebElement nextPageButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='下一页']")));
            nextPageButton.click();
            Thread.sleep(10000);*/

            // 打印第二页搜索结果的标题和链接，并统计顶级域名出现的次数
            System.out.println("结果列表");
            Map<String, Integer> domainCounts = printSearchResults(driver);
            System.out.println("结果统计");
            for (Map.Entry<String, Integer> entry : domainCounts.entrySet()) {
                System.out.println(entry.getKey() + "  --> " + entry.getValue());
            }

            System.out.println("-------------<搜索Selenium>---------------");
            // 搜索关键词 "Selenium"
            search(driver, "Selenium");
            Thread.sleep(10000);

            // 打印搜索结果的标题和链接，并统计顶级域名出现的次数
            System.out.println("结果列表");
            domainCounts = printSearchResults(driver);
            System.out.println("结果统计");
            for (Map.Entry<String, Integer> entry : domainCounts.entrySet()) {
                System.out.println(entry.getKey() + "  --> " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭浏览器
            driver.quit();
        }
    }

    // 执行搜索操作
    public static void search(WebDriver driver, String keyword) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        searchInput.clear();
        searchInput.sendKeys(keyword);
        searchInput.submit();
    }

    // 打印搜索结果的标题和链接，并统计顶级域名出现的次数
    public static Map<String, Integer> printSearchResults(WebDriver driver) {
        Map<String, Integer> domainCounts = new HashMap<>();
        List<WebElement> resultItems = driver.findElements(By.xpath("//li[@class='b_algo']"));
        for (WebElement resultItem : resultItems) {
            WebElement titleElement = resultItem.findElement(By.tagName("h2"));
            String title = titleElement.getText();

            WebElement linkElement = resultItem.findElement(By.tagName("a"));
            String url = linkElement.getAttribute("href");
            System.out.println(title + "  --> " + url);
            String domain = getDomain(url);
            if (domainCounts.containsKey(domain)) {
                domainCounts.put(domain, domainCounts.get(domain) + 1);
            } else {
                domainCounts.put(domain, 1);
            }
        }
        return domainCounts;
    }

    // 提取链接的顶级域名
    public static String getDomain(String url) {
        String domain = "";
        if (url.startsWith("http://") || url.startsWith("https://")) {
            domain = url.split("//")[1];
        } else {
            domain = url;
        }
        domain = domain.split("/")[0];
        if (domain.startsWith("www.")) {
            domain = domain.substring(4, domain.length());
        } else {
            int dotIndex = domain.indexOf(".");
            if (dotIndex != -1) {
                domain = domain.substring(dotIndex + 1, domain.length());
            }
        }
        return domain;
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